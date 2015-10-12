/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.hq;

import static io.logspace.agent.api.HttpStatusCode.NotFound;
import static java.text.MessageFormat.format;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.client.HttpResponseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.squareup.tape.FileObjectQueue;

import io.logspace.agent.api.*;
import io.logspace.agent.api.AgentControllerDescription.Parameter;
import io.logspace.agent.api.event.Event;
import io.logspace.agent.api.order.AgentControllerCapabilities;
import io.logspace.agent.api.order.AgentControllerOrder;
import io.logspace.agent.api.order.AgentOrder;
import io.logspace.agent.api.order.TriggerType;
import io.logspace.agent.api.util.ConsoleWriter;
import io.logspace.agent.impl.AbstractAgentController;
import io.logspace.agent.scheduling.AgentExecutor;
import io.logspace.agent.scheduling.AgentScheduler;

public class HqAgentController extends AbstractAgentController implements AgentExecutor {

    private static final int DEFAULT_UPLOAD_SIZE = 1000;
    private static final int DEFAULT_COMMIT_DELAY = 300;

    private static final String BASE_URL_PARAMETER = "base-url";
    private static final String SPACE_TOKEN_PARAMETER = "space-token";
    private static final String QUEUE_DIRECTORY_PARAMETER = "queue-directory";
    private static final String HQ_COMMUNICATION_INTERVAL_PARAMETER = "hq-communication-interval";
    private static final String HQ_COMMUNICATION_INTERVAL_DEFAULT_VALUE = "60";

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private boolean modifiedAgents;

    private FileObjectQueue<Event> persistentQueue;

    private HqClient hqClient;

    private AgentScheduler agentScheduler;

    private CommitRunnable commitRunnable;
    private int uploadSize = DEFAULT_UPLOAD_SIZE;
    private AgentControllerOrder agentControllerOrder;

    public HqAgentController(AgentControllerDescription agentControllerDescription) {
        this.setId(agentControllerDescription.getId());

        this.initializePersistentQueue(agentControllerDescription);
        this.initializeHqClient(agentControllerDescription);
        this.initializeCommitRunnable();
        this.initializeAgentScheduler(agentControllerDescription);
    }

    public static void install(String id, String baseUrl, String queueDirectory, String spaceToken) {
        AgentControllerDescription description = new AgentControllerDescription();

        description.setClassName(HqAgentController.class.getName());
        description.setId(id);
        description.addParameter(Parameter.create(BASE_URL_PARAMETER, baseUrl));
        description.addParameter(Parameter.create(QUEUE_DIRECTORY_PARAMETER, queueDirectory));
        description.addParameter(Parameter.create(SPACE_TOKEN_PARAMETER, spaceToken));

        AgentControllerProvider.setDescription(description);
    }

    private static String createQueueFileName(String agentControllerId) {
        return MessageFormat.format("logspace-{0}.dat", agentControllerId);
    }

    private static File getFile(String path, String agentControllerId) {
        String resolvedPath = resolveProperties(path);

        File file = new File(resolvedPath);

        try {
            file = file.getCanonicalFile();
        } catch (IOException e) {
            // ignore this
        }

        return new File(file.getAbsoluteFile(), createQueueFileName(agentControllerId));
    }

    private static String resolveProperties(String value) {
        Pattern pattern = Pattern.compile("\\$\\{(.*?)\\}");
        Matcher matcher = pattern.matcher(value);

        StringBuffer stringBuffer = new StringBuffer();
        while (matcher.find()) {
            String propertyName = matcher.group(1);

            String propertyValue = System.getProperty(propertyName);
            if (propertyValue == null) {
                throw new AgentControllerException("Could not resolve property '" + propertyName + "' in '" + value + "'.");
            }

            matcher.appendReplacement(stringBuffer, propertyValue.replace('\\', '/'));
        }

        matcher.appendTail(stringBuffer);

        return stringBuffer.toString();
    }

    @Override
    public void executeScheduledAgent(AgentOrder agentOrder) {
        Agent agent = this.getAgent(agentOrder.getId());
        if (agent == null) {
            this.logger.error("Could not execute agent with ID '" + agentOrder.getId() + "', because it does not exist.");
            return;
        }

        if (!(agent instanceof SchedulerAgent)) {
            this.logger.error("Could not execute agent with ID '" + agentOrder.getId() + "', because it is not a scheduled agent.");
            return;
        }

        ((SchedulerAgent) agent).execute(agentOrder);
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
        return agentTriggerType == TriggerType.Application || agentTriggerType == TriggerType.Scheduler;
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
    public void update(Date nextFireTime) {
        try {
            this.uploadCapabilities();
        } catch (ConnectException cex) {
            this.logger.error("Could not upload capabilities because the HQ was not available: {} - Will retry at {}",
                cex.getMessage(), nextFireTime);
            // no need to try downloading as well
            return;
        } catch (IOException ioex) {
            this.logger.error("Failed to upload capabilities. Will retry at " + nextFireTime, ioex);
        }

        try {
            this.downloadOrder();
        } catch (ConnectException cex) {
            this.logger.error("Could not download orders because the HQ was not available: {} - Will retry at {}", cex.getMessage(),
                nextFireTime);
        } catch (HttpResponseException hrex) {
            if (NotFound.matches(hrex.getStatusCode())) {
                this.logger.error("There was no order available: {} - Will retry at {}", hrex.getMessage(), nextFireTime);
            } else {
                this.logger.error("Failed to download order. Will retry at " + nextFireTime, hrex);
            }
        } catch (IOException ioex) {
            this.logger.error("Failed to download order. Will retry at " + nextFireTime, ioex);
        }
    }

    @Override
    protected void onAgentRegistered(Agent agent) {
        super.onAgentRegistered(agent);

        this.modifiedAgents = true;

        if (this.agentControllerOrder == null) {
            return;
        }

        AgentOrder agentOrder = this.agentControllerOrder.getAgentOrder(agent.getId());
        if (agentOrder != null) {
            this.agentScheduler.applyAgentOrder(agentOrder);
        }
    }

    @Override
    protected void onAgentUnregistered(Agent agent) {
        super.onAgentUnregistered(agent);

        this.modifiedAgents = true;

        if (this.agentControllerOrder == null) {
            return;
        }

        AgentOrder agentOrder = this.agentControllerOrder.getAgentOrder(agent.getId());
        if (agentOrder != null) {
            this.agentScheduler.removeAgentOrder(agentOrder);
        }
    }

    protected void performCommit(long commitDelayInSeconds) {
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
            this.logger.error("Could not upload events because the HQ was not available: {}. Trying in {} second(s) again.",
                cex.getMessage(), commitDelayInSeconds);
        } catch (IOException ioex) {
            this.logger.error("Failed to upload events. Trying in " + commitDelayInSeconds + " second(s) again.", ioex);
        } catch (UploadException ue) {
            this.logger.error("The HQ did not accept events: {} Trying in {} second(s) again.", ue.getMessage(), commitDelayInSeconds);
        }
    }

    private void downloadOrder() throws IOException {
        AgentControllerOrder order = this.hqClient.downloadOrder();
        if (order == null) {
            return;
        }

        this.logger.info("Received new AgentControllerOrder from HQ.");

        this.agentControllerOrder = order;
        this.agentScheduler.applyAgentControllerOrder(this.agentControllerOrder, this.getAgentIds());

        Integer commitDelay = this.agentControllerOrder.getCommitMaxSeconds().orElse(DEFAULT_COMMIT_DELAY);
        this.logger.info("Committing after {} second(s).", commitDelay);
        this.setCommitDelayInSeconds(commitDelay);
    }

    private List<Event> getEventsForUpload() {
        this.logger.debug("Retrieving events to be committed.");

        synchronized (this.persistentQueue) {
            return this.persistentQueue.peek(this.uploadSize);
        }
    }

    private void initializeAgentScheduler(AgentControllerDescription agentControllerDescription) {
        int hqCommunicationInterval = Integer.parseInt(agentControllerDescription
            .getParameterValue(HQ_COMMUNICATION_INTERVAL_PARAMETER, HQ_COMMUNICATION_INTERVAL_DEFAULT_VALUE));
        this.agentScheduler = new AgentScheduler(this, hqCommunicationInterval);

        // execute the first update within the initializing thread to ensure we're fully initialized
        this.update(new Date(System.currentTimeMillis() + hqCommunicationInterval));
    }

    private void initializeCommitRunnable() {
        this.commitRunnable = new CommitRunnable();
        this.setCommitDelayInSeconds(DEFAULT_COMMIT_DELAY);
        new Thread(this.commitRunnable, "Logspace-Commit-Thread").start();
    }

    private void initializeHqClient(AgentControllerDescription agentControllerDescription) {
        String baseUrl = agentControllerDescription.getParameterValue(BASE_URL_PARAMETER);
        String spaceToken = agentControllerDescription.getParameterValue(SPACE_TOKEN_PARAMETER);
        this.hqClient = new HqClient(baseUrl, this.getId(), spaceToken);
    }

    private void initializePersistentQueue(AgentControllerDescription agentControllerDescription) {
        try {
            String queueDirectoryParameter = agentControllerDescription.getParameterValue(QUEUE_DIRECTORY_PARAMETER);
            if (queueDirectoryParameter == null) {
                throw new AgentControllerInitializationException(
                    format("No queue directory is configured. Did you set parameter ''{0}''?", QUEUE_DIRECTORY_PARAMETER));
            }

            File queueFile = getFile(queueDirectoryParameter, agentControllerDescription.getId());
            ConsoleWriter.writeSystem(format("Using queue file ''{0}''.", queueFile.getPath()));
            this.persistentQueue = new FileObjectQueue<Event>(queueFile, new TapeEventConverter());
        } catch (Exception e) {
            throw new AgentControllerInitializationException("Could not initialize queue file.", e);
        }
    }

    private void purgeUploadedEvents(List<Event> events) throws IOException {
        this.logger.debug("Removing {} events from persistent queue.", events.size());

        synchronized (this.persistentQueue) {
            this.persistentQueue.remove(events.size());
        }
    }

    private void setCommitDelayInSeconds(int commitDelayInSeconds) {
        this.commitRunnable.setCommitDelayInMilliseconds(SECONDS.toMillis(commitDelayInSeconds));
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
        this.hqClient.uploadEvents(events);
        this.logger.info("Successfully uploaded {} event(s) to the HQ.", events.size());
    }

    private class CommitRunnable implements Runnable {

        private boolean run = true;
        private Thread executorThread;
        private long commitDelayInMilliseconds;

        public void commit() {
            this.wakeUp();
        }

        @Override
        public void run() {
            this.executorThread = Thread.currentThread();

            while (true) {
                long commitDelayInSeconds = MILLISECONDS.toSeconds(this.commitDelayInMilliseconds);
                HqAgentController.this.performCommit(commitDelayInSeconds);

                synchronized (this) {
                    if (!this.run) {
                        break;
                    }
                    this.sleep(this.commitDelayInMilliseconds);
                }
            }

            HqAgentController.this.logger.info("CommitRunnable: Stopped.");
        }

        public void setCommitDelayInMilliseconds(long commitDelay) {
            boolean commitDelayChanged = this.commitDelayInMilliseconds != commitDelay;

            this.commitDelayInMilliseconds = commitDelay;

            if (commitDelayChanged) {
                this.wakeUp();
            }
        }

        public void stop() {
            HqAgentController.this.logger.info("CommitRunnable: Stopping");

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
                HqAgentController.this.logger.debug("CommitRunnable: Waiting for {} ms", duration);
                this.wait(duration);
                HqAgentController.this.logger.debug("CommitRunnable: Resuming");
            } catch (InterruptedException e) {
                // do nothing
            }
        }

        private synchronized void wakeUp() {
            this.notifyAll();
        }
    }
}
