/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.hq;

import static io.logspace.agent.api.HttpStatusCode.NotFound;
import static java.util.concurrent.TimeUnit.SECONDS;
import io.logspace.agent.api.Agent;
import io.logspace.agent.api.AgentControllerDescription;
import io.logspace.agent.api.AgentControllerDescription.Parameter;
import io.logspace.agent.api.event.Event;
import io.logspace.agent.api.order.AgentControllerCapabilities;
import io.logspace.agent.api.order.AgentControllerOrder;
import io.logspace.agent.api.order.AgentOrder;
import io.logspace.agent.api.order.TriggerType;
import io.logspace.agent.impl.AbstractAgentController;
import io.logspace.agent.impl.AgentControllerException;
import io.logspace.agent.impl.AgentControllerInitializationException;
import io.logspace.agent.impl.AgentControllerProvider;
import io.logspace.agent.scheduling.AgentExecutor;
import io.logspace.agent.scheduling.AgentScheduler;

import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.util.Collection;
import java.util.List;

import org.apache.http.client.HttpResponseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.squareup.tape.FileObjectQueue;

public class HqAgentController extends AbstractAgentController implements AgentExecutor {

    private static final int DEFAULT_UPLOAD_SIZE = 1000;

    private static final String BASE_URL_PARAMETER = "base-url";
    private static final String SPACE_TOKEN_PARAMETER = "space-token";
    private static final String QUEUE_FILE_PARAMETER = "queue-file";
    private static final String HQ_COMMUNICATION_INTERVAL_PARAMETER = "hq-communication-interval";
    private static final String HQ_COMMUNICATION_INTERVAL_DEFAULT_VALUE = "60";

    private static final int DEFAULT_COMMIT_DELAY = 300;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private boolean modifiedAgents;

    private FileObjectQueue<Event> persistentQueue;

    private HqClient hqClient;

    private AgentScheduler agentScheduler;

    private CommitRunnable commitRunnable;
    private int uploadSize = DEFAULT_UPLOAD_SIZE;

    public HqAgentController(AgentControllerDescription agentControllerDescription) {
        this.setId(agentControllerDescription.getId());

        String baseUrl = agentControllerDescription.getParameterValue(BASE_URL_PARAMETER);
        String spaceToken = agentControllerDescription.getParameterValue(SPACE_TOKEN_PARAMETER);
        this.hqClient = new HqClient(baseUrl, this.getId(), spaceToken);

        int hqCommunicationInterval = Integer.parseInt(agentControllerDescription.getParameterValue(
                HQ_COMMUNICATION_INTERVAL_PARAMETER, HQ_COMMUNICATION_INTERVAL_DEFAULT_VALUE));
        this.agentScheduler = new AgentScheduler(this, hqCommunicationInterval);

        try {
            String queueFile = agentControllerDescription.getParameterValue(QUEUE_FILE_PARAMETER);
            if (queueFile == null) {
                throw new AgentControllerInitializationException("No queue file is configured. Did you set parameter '"
                        + QUEUE_FILE_PARAMETER + "'?");
            }

            this.persistentQueue = new FileObjectQueue<Event>(new File(queueFile), new TapeEventConverter());
        } catch (Exception e) {
            throw new AgentControllerInitializationException("Could not initialize queue file.", e);
        }

        this.commitRunnable = new CommitRunnable();
        this.setCommitDelay(DEFAULT_COMMIT_DELAY);
        new Thread(this.commitRunnable, "Logspace-Commit-Thread").start();
    }

    public static void install(String id, String baseUrl, String queueFile, String spaceToken) {
        AgentControllerDescription description = AgentControllerDescription.withClass(HqAgentController.class);
        description.setId(id);
        description.addParameter(Parameter.create(BASE_URL_PARAMETER, baseUrl));
        description.addParameter(Parameter.create(QUEUE_FILE_PARAMETER, queueFile));
        description.addParameter(Parameter.create(SPACE_TOKEN_PARAMETER, spaceToken));

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
        this.logger.info("{} - Flushing events", this.getId());
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
                this.persistentQueue.add(eachEvent);
            }

            if (this.persistentQueue.size() >= this.uploadSize) {
                this.commitRunnable.commit();
            }
        }
    }

    @Override
    public void send(Event event) {
        synchronized (this.persistentQueue) {
            this.persistentQueue.add(event);

            if (this.persistentQueue.size() >= this.uploadSize) {
                this.commitRunnable.commit();
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
        } catch (ConnectException cex) {
            this.logger.error("Could not upload capabilities because the HQ was not available: {}", cex.getMessage());
            // no need to try downloading as well
            return;
        } catch (IOException ioex) {
            this.logger.error("Failed to upload capabilities.", ioex);
        }

        try {
            this.downloadOrder();
        } catch (ConnectException cex) {
            this.logger.error("Could not download orders because the HQ was not available: {}", cex.getMessage());
        } catch (HttpResponseException hrex) {
            if (NotFound.matches(hrex.getStatusCode())) {
                this.logger.error("There was no order available: {}", hrex.getMessage());
            } else {
                this.logger.error("Failed to download order.", hrex);
            }
        } catch (IOException ioex) {
            this.logger.error("Failed to download order.", ioex);
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
            do {
                List<Event> eventsForUpload = this.getEventsForUpload();
                if (eventsForUpload == null || eventsForUpload.isEmpty()) {
                    return;
                }

                this.uploadEvents(eventsForUpload);
                this.purgeUploadedEvents(eventsForUpload);
            } while (this.persistentQueue.size() >= this.uploadSize);
        } catch (ConnectException cex) {
            this.logger.error("Could not upload events because the HQ was not available: {}", cex.getMessage());
        } catch (IOException ioex) {
            this.logger.error("Failed to download order.", ioex);
        }
    }

    private void downloadOrder() throws IOException {
        AgentControllerOrder agentControllerOrder = this.hqClient.downloadOrder();
        if (agentControllerOrder == null) {
            return;
        }

        this.agentScheduler.applyOrder(agentControllerOrder, this.getAgentIds());

        Integer commitDelay = agentControllerOrder.getCommitMaxSeconds().orElse(DEFAULT_COMMIT_DELAY);
        this.logger.info("Committing after {} seconds.", commitDelay);
        this.setCommitDelay(commitDelay);
    }

    private List<Event> getEventsForUpload() {
        this.logger.debug("Retrieving events to be committed.");

        synchronized (this.persistentQueue) {
            return this.persistentQueue.peek(this.uploadSize);
        }
    }

    private void purgeUploadedEvents(List<Event> events) throws IOException {
        this.logger.debug("Removing {} events from persistent queue.", events.size());

        synchronized (this.persistentQueue) {
            this.persistentQueue.remove(events.size());
        }
    }

    private void setCommitDelay(int commitDelay) {
        this.commitRunnable.setCommitDelay(SECONDS.toMillis(commitDelay));
    }

    private void uploadCapabilities() throws IOException {
        if (!this.modifiedAgents) {
            return;
        }

        AgentControllerCapabilities capabilities = this.getCapabilities();
        this.hqClient.uploadCapabilities(capabilities);
        this.modifiedAgents = false;
    }

    private void uploadEvents(Collection<Event> events) throws IOException {
        this.logger.info("Committing {} events to HQ.", events.size());

        this.hqClient.uploadEvents(events);
    }

    private class CommitRunnable implements Runnable {

        private boolean run = true;
        private Thread executorThread;
        private long commitDelay;

        public void commit() {
            this.wakeUp();
        }

        @Override
        public void run() {
            this.executorThread = Thread.currentThread();

            while (true) {
                HqAgentController.this.performCommit();

                synchronized (this) {
                    if (!this.run) {
                        break;
                    }
                    this.sleep(this.commitDelay);
                }
            }

            HqAgentController.this.logger.debug("CommitRunnable: Stopped.");
        }

        public void setCommitDelay(long commitDelay) {
            boolean commitDelayChanged = this.commitDelay != commitDelay;

            this.commitDelay = commitDelay;

            if (commitDelayChanged) {
                this.wakeUp();
            }
        }

        public void stop() {
            HqAgentController.this.logger.debug("CommitRunnable: Stopping");

            synchronized (this) {
                this.run = false;
                this.wakeUp();
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

        private void sleep(long duration) {
            try {
                this.wait(duration);
            } catch (InterruptedException e) {
                // do nothing
            }
        }

        private synchronized void wakeUp() {
            this.notifyAll();
        }
    }
}