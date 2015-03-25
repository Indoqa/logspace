/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.impl;

import io.logspace.agent.api.Agent;
import io.logspace.agent.api.AgentControllerDescription;
import io.logspace.agent.api.AgentControllerDescription.Parameter;
import io.logspace.agent.api.event.Event;
import io.logspace.agent.api.order.AgentControllerCapabilities;
import io.logspace.agent.api.order.AgentControllerOrder;
import io.logspace.agent.api.order.AgentOrder;
import io.logspace.agent.api.order.TriggerType;
import io.logspace.agent.hq.HqClient;
import io.logspace.agent.hq.TapeEventConverter;
import io.logspace.agent.scheduling.AgentExecutor;
import io.logspace.agent.scheduling.AgentScheduler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.squareup.tape.FileObjectQueue;
import com.squareup.tape.ObjectQueue;

public class HqAgentController extends AbstractAgentController implements AgentExecutor {

    private static final String BASE_URL_PARAMETER = "base-url";
    private static final String QUEUE_FILE_PARAMETER = "queue-file";
    private static final String HQ_COMMUNICATION_INTERVAL_PARAMETER = "hq-communication-interval";
    private static final String HQ_COMMUNICATION_INTERVAL_DEFAULT_VALUE = "60";

    private static final int MAX_RESCHEDULE_COUNT = 100;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private boolean modifiedAgents;

    private ObjectQueue<Event> persistentQueue;
    private List<Event> volatileQueue;

    private HqClient hqClient;

    private AgentScheduler agentScheduler;

    private Integer commitMaxCount;
    private Integer commitMaxSeconds;

    private CommitRunnable commitRunnable;

    public HqAgentController(AgentControllerDescription agentControllerDescription) {
        this.setId(agentControllerDescription.getId());

        String baseUrl = agentControllerDescription.getParameterValue(BASE_URL_PARAMETER);
        this.hqClient = new HqClient(baseUrl, this.getId());

        int hqCommunicationInterval = Integer.parseInt(agentControllerDescription.getParameterValue(
                HQ_COMMUNICATION_INTERVAL_PARAMETER, HQ_COMMUNICATION_INTERVAL_DEFAULT_VALUE));
        this.agentScheduler = new AgentScheduler(this, hqCommunicationInterval);

        this.commitRunnable = new CommitRunnable();
        new Thread(this.commitRunnable).start();

        try {
            this.volatileQueue = new ArrayList<Event>();

            String queueFile = agentControllerDescription.getParameterValue(QUEUE_FILE_PARAMETER);
            if (queueFile == null) {
                throw new AgentControllerInitializationException("No queue file is configured. Did you set parameter '"
                        + QUEUE_FILE_PARAMETER + "'?");
            }

            this.persistentQueue = new FileObjectQueue<Event>(new File(queueFile), new TapeEventConverter());

            if (this.persistentQueue.size() != 0) {
                this.logger.info("Found {} events in the persistent queue. Scheduling committing.", this.persistentQueue.size());
                this.commitRunnable.schedule(1000);
            }
        } catch (Exception e) {
            throw new AgentControllerInitializationException("Could not initialize queue file.", e);
        }
    }

    public static void install(String id, String baseUrl, String queueFile) {
        AgentControllerDescription description = AgentControllerDescription.withClass(HqAgentController.class);
        description.setId(id);
        description.addParameter(Parameter.create(BASE_URL_PARAMETER, baseUrl));
        description.addParameter(Parameter.create(QUEUE_FILE_PARAMETER, queueFile));

        AgentControllerProvider.setDescription(description);
    }

    @Override
    public void executeAgent(AgentOrder agentOrder) {
        Agent agent = this.getAgent(agentOrder.getId());
        if (agent == null) {
            this.logger.error("Could not execute agent with ID '" + agentOrder.getId() + "', because it does not exist.");
            return;
        }

        agent.execute(agentOrder);
    }

    @Override
    public void flush() {
        this.commitRunnable.commit();
    }

    @Override
    public boolean isAgentEnabled(String agentId) {
        AgentOrder agentOrder = this.agentScheduler.getAgentOrder(agentId);
        if (agentOrder == null) {
            return false;
        }

        TriggerType agentTriggerType = agentOrder.getTriggerType();
        return agentTriggerType == TriggerType.Event || agentTriggerType == TriggerType.Cron;
    }

    @Override
    public void send(Collection<Event> events) {
        synchronized (this.persistentQueue) {
            for (Event eachEvent : events) {
                this.send(eachEvent);
            }
        }
    }

    @Override
    public void send(Event event) {
        synchronized (this.persistentQueue) {
            this.persistentQueue.add(event);
            this.volatileQueue.add(event);

            if (this.commitMaxCount != null && this.volatileQueue.size() >= this.commitMaxCount.intValue()) {
                this.commitRunnable.commit();
            }

            if (this.commitMaxSeconds != null) {
                this.commitRunnable.schedule(this.commitMaxSeconds.intValue());
            }
        }
    }

    @Override
    public void shutdown() {
        this.logger.info("Performing shutdown.");

        try {
            this.agentScheduler.stop();
            this.logger.debug("Scheduler is stopped.");
        } catch (AgentControllerException acex) {
            this.logger.error("Failed to stop scheduler.", acex);
        }

        try {
            this.commitRunnable.stop();
            this.logger.debug("Commit runnable is stopped.");
        } catch (Exception ex) {
            this.logger.error("Failed to commit runnable.", ex);
        }

        try {
            this.hqClient.close();
            this.logger.debug("HQ client is closed.");
        } catch (IOException ioex) {
            this.logger.error("Failed to close HTTP client.", ioex);
        }

        super.shutdown();
    }

    @Override
    public void update() {
        try {
            this.uploadCapabilities();
        } catch (IOException ioex) {
            this.logger.error("Failed to communicate with the HQ.", ioex);
        }

        try {
            this.downloadOrder();
        } catch (IOException ioex) {
            this.logger.error("Failed to download order", ioex);
        }
    }

    @Override
    protected void onAgentRegistered(Agent agent) {
        super.onAgentRegistered(agent);

        this.modifiedAgents = true;
    }

    @Override
    protected void onAgentUnregistered(Agent agent) {
        super.onAgentUnregistered(agent);

        this.modifiedAgents = true;
    }

    protected void performCommit() {
        try {
            this.logger.info("Retrieving events to be uploaded.");
            Map<String, Event> eventsForUpload = this.getEventsForUpload();
            if (eventsForUpload == null) {
                return;
            }

            this.logger.info("Uploading {} events to HQ.", eventsForUpload.size());
            this.uploadEvents(eventsForUpload.values());

            this.purgeUploadedEvents(eventsForUpload);
        } catch (IOException e) {
            this.logger.error("Failed to upload events.", e);
            this.purgeUploadedEvents(Collections.<String, Event> emptyMap());
        }
    }

    private void downloadOrder() throws IOException {
        AgentControllerOrder agentControllerOrder = this.hqClient.downloadOrder();
        if (agentControllerOrder == null) {
            return;
        }

        this.agentScheduler.applyOrder(agentControllerOrder, this.getAgentIds());

        this.commitMaxCount = agentControllerOrder.getCommitMaxCount().orElse(null);

        if (agentControllerOrder.getCommitMaxSeconds().isPresent()) {
            this.commitMaxSeconds = agentControllerOrder.getCommitMaxSeconds().get() * 1000;
        }

        this.logger.info("Committing after {} events or {} seconds.", this.commitMaxCount, this.commitMaxSeconds);
    }

    private Map<String, Event> getEventsForUpload() {
        synchronized (this.persistentQueue) {
            if (this.volatileQueue.isEmpty()) {
                if (this.persistentQueue.size() == 0) {
                    // apparently nothing to do -> shouldn't happen
                    this.logger.warn("Ignoring commit because there are no pending events");
                    return null;
                }

                // upload queue is empty and persistent not -> trouble
                // simple approach: just re-send the first event, this will trigger more
                this.logger.warn("Volatile queue is empty but persistent queue is not. Re-enqueuing one event.");
                Event event = this.persistentQueue.peek();
                this.send(event);
                this.persistentQueue.remove();
            }

            Map<String, Event> eventsForUpload = new HashMap<String, Event>(this.volatileQueue.size());
            for (Event eachEvent : this.volatileQueue) {
                eventsForUpload.put(eachEvent.getId(), eachEvent);
            }

            this.volatileQueue.clear();

            return eventsForUpload;
        }
    }

    private void purgeUploadedEvents(Map<String, Event> eventsForUpload) {
        this.logger.debug("Purging queues.");

        synchronized (this.persistentQueue) {
            while (!eventsForUpload.isEmpty()) {
                Event persistentEvent = this.persistentQueue.peek();

                if (eventsForUpload.remove(persistentEvent.getId()) == null) {
                    // we didn't upload this event
                    if (this.volatileQueue.size() < MAX_RESCHEDULE_COUNT) {
                        this.send(persistentEvent);
                    } else {
                        // we already have too many events in the volatile queue -> stop
                        break;
                    }
                }

                this.persistentQueue.remove();
            }

            if (this.persistentQueue.size() != 0) {
                // trouble -> we have pending events
                this.logger.warn("Persistent queue contains events while the volatile queue is already empty.");
                // simple approach: commit again
                this.flush();
            }
        }
    }

    private void uploadCapabilities() throws IOException {
        if (!this.modifiedAgents) {
            return;
        }

        AgentControllerCapabilities capabilities = this.getCapabilities();
        this.modifiedAgents = false;

        this.hqClient.uploadCapabilities(capabilities);
    }

    private void uploadEvents(Collection<Event> events) throws IOException {
        this.hqClient.uploadEvents(events);
    }

    private class CommitRunnable implements Runnable {

        private boolean run = true;
        private long nextCommitTime = 0;
        private Thread executorThread;

        public void commit() {
            this.nextCommitTime = System.currentTimeMillis();

            synchronized (this) {
                this.notify();
            }
        }

        @Override
        public void run() {
            this.executorThread = Thread.currentThread();

            while (this.run) {
                if (this.nextCommitTime == 0) {
                    synchronized (this) {
                        try {
                            this.wait();
                        } catch (InterruptedException e) {
                            // do nothing
                        }
                    }

                    continue;
                }

                long delay = this.nextCommitTime - System.currentTimeMillis();
                if (delay > 0) {
                    synchronized (this) {
                        try {
                            this.wait(delay);
                        } catch (InterruptedException e) {
                            // do nothing
                        }
                    }
                }

                this.nextCommitTime = 0;
                HqAgentController.this.performCommit();
            }
        }

        public void schedule(int maxCommitDelay) {
            long commitTime = System.currentTimeMillis() + maxCommitDelay;
            if (this.nextCommitTime > 0 && commitTime >= this.nextCommitTime) {
                return;
            }

            this.nextCommitTime = commitTime;
            synchronized (this) {
                this.notify();
            }
        }

        public void stop() {
            this.run = false;

            synchronized (this) {
                this.notify();
            }

            while (true) {
                try {
                    this.executorThread.join();
                    break;
                } catch (InterruptedException e) {
                    // do nothing
                }
            }
        }
    }
}