/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.impl;

import static org.apache.http.entity.ContentType.APPLICATION_JSON;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;
import io.logspace.agent.api.Agent;
import io.logspace.agent.api.AgentControllerDescription;
import io.logspace.agent.api.AgentControllerDescription.Parameter;
import io.logspace.agent.api.event.Event;
import io.logspace.agent.api.json.AgentControllerCapabilitiesJsonSerializer;
import io.logspace.agent.api.json.EventJsonSerializer;
import io.logspace.agent.api.order.AgentControllerCapabilities;
import io.logspace.agent.api.order.AgentControllerOrder;
import io.logspace.agent.api.order.AgentOrder;
import io.logspace.agent.api.order.TriggerType;
import io.logspace.agent.hq.AgentControllerOrderResponseHandler;
import io.logspace.agent.hq.UploadCapabilitiesResponseHandler;
import io.logspace.agent.hq.UploadEventsResponseHandler;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HqAgentController extends AbstractAgentController {

    private static final String BASE_URL_PARAMETER = "base-url";

    private static final String HQ_COMMUNICATION_INTERVAL_PARAMETER = "hq-communication-interval";
    private static final String HQ_COMMUNICATION_INTERVAL_DEFAULT_VALUE = "60";

    private static final String KEY_AGENT_ID = "agent-id";
    private static final String KEY_AGENT_CONTROLLER = "agent-controller";
    private static final String LOGSPACE_SCHEDULER_GROUP = "logspace";
    private static final String AGENT_SCHEDULER_GROUP = "logspace-agents";

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private CloseableHttpClient httpClient;

    private String baseUrl;

    private Scheduler scheduler;

    private int hqCommunicationInterval;

    private boolean modifiedAgents;

    private final Map<String, AgentOrder> agentOrders = new HashMap<String, AgentOrder>();

    public HqAgentController(AgentControllerDescription agentControllerDescription) {
        this.baseUrl = agentControllerDescription.getParameterValue(BASE_URL_PARAMETER);

        if (this.baseUrl == null || this.baseUrl.trim().length() == 0) {
            throw new AgentControllerInitializationException("The base URL must not be empty!");
        }

        this.hqCommunicationInterval = Integer.parseInt(agentControllerDescription.getParameterValue(
                HQ_COMMUNICATION_INTERVAL_PARAMETER, HQ_COMMUNICATION_INTERVAL_DEFAULT_VALUE));

        this.initialize();
    }

    public static void install(String baseUrl) {
        AgentControllerDescription description = AgentControllerDescription.withClass(HqAgentController.class);
        description.addParameter(Parameter.create(BASE_URL_PARAMETER, baseUrl));

        AgentControllerProvider.setDescription(description);
    }

    private static StringEntity toJsonEntity(Collection<Event> event) throws IOException {
        return new StringEntity(EventJsonSerializer.toJson(event), APPLICATION_JSON);
    }

    private static StringEntity toJSonEntity(AgentControllerCapabilities capabilities) throws IOException {
        return new StringEntity(AgentControllerCapabilitiesJsonSerializer.toJson(capabilities), APPLICATION_JSON);
    }

    @Override
    public boolean isAgentEnabled(String agentId) {
        AgentOrder agentOrder = this.agentOrders.get(agentId);
        if (agentOrder == null) {
            return false;
        }

        TriggerType agentTriggerType = agentOrder.getTriggerType();
        return agentTriggerType == TriggerType.Event || agentTriggerType == TriggerType.Cron;
    }

    @Override
    public void send(Collection<Event> events) {
        try {
            this.sendEvents(events);
        } catch (IOException e) {
            this.logger.error("Cannot send events to HQ.", e);
        }
    }

    @Override
    public void shutdown() {
        this.logger.info("Performing shutdown.");

        try {
            this.scheduler.shutdown(true);
            this.logger.debug("All scheduler jobs stopped.");
        } catch (SchedulerException sex) {
            this.logger.error("Failed to stop scheduler.", sex);
        }

        try {
            this.httpClient.close();
            this.logger.debug("HTTP client closed.");
        } catch (IOException ioex) {
            this.logger.error("Failed to close HTTP client.", ioex);
        }

        super.shutdown();
    }

    protected void callHQ() {
        try {
            this.uploadCapabilities();
        } catch (IOException ioex) {
            this.logger.error("Failed to communicate with the HQ.", ioex);
        }

        try {
            this.downloadOrders();
        } catch (IOException ioex) {
            this.logger.error("Failed to download order", ioex);
        } catch (SchedulerException sex) {
            this.logger.error("Failed to apply order.", sex);
        }
    }

    protected void executeAgent(String agentId) {
        AgentOrder agentOrder = this.agentOrders.get(agentId);
        if (agentOrder == null) {
            this.logger.error("Could not execute agent with ID '" + agentId + "', because there is no order for it.");
            return;
        }

        Agent agent = this.getAgent(agentId);
        if (agent == null) {
            this.logger.error("Could not execute agent with ID '" + agentId + "', because it does not exist.");
            return;
        }

        agent.execute(agentOrder);
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

    private void applyOrder(AgentControllerOrder agentControllerOrder) throws SchedulerException {
        Set<TriggerKey> triggerKeys = this.scheduler.getTriggerKeys(GroupMatcher.<TriggerKey> groupEquals(AGENT_SCHEDULER_GROUP));
        for (TriggerKey eachTriggerKey : triggerKeys) {
            this.scheduler.unscheduleJob(eachTriggerKey);
        }
        this.agentOrders.clear();

        for (AgentOrder eachAgentOrder : agentControllerOrder.getAgentOrders()) {
            this.agentOrders.put(eachAgentOrder.getId(), eachAgentOrder);

            if (eachAgentOrder.getTriggerType() == TriggerType.Cron) {
                JobDataMap jobDataMap = new JobDataMap();
                jobDataMap.put(KEY_AGENT_CONTROLLER, this);
                jobDataMap.put(KEY_AGENT_ID, eachAgentOrder.getId());

                JobDetail job = newJob(AgentExecutionJob.class).withIdentity(eachAgentOrder.getId(), AGENT_SCHEDULER_GROUP)
                        .usingJobData(jobDataMap).build();

                Trigger trigger = newTrigger().withIdentity(eachAgentOrder.getId(), AGENT_SCHEDULER_GROUP).startNow()
                        .withSchedule(CronScheduleBuilder.cronSchedule(eachAgentOrder.getTriggerParameter())).build();

                this.scheduler.scheduleJob(job, trigger);
            }
        }
    }

    private void downloadOrders() throws IOException, SchedulerException {
        HttpGet httpGet = new HttpGet(this.baseUrl + "/orders/" + this.getId());

        AgentControllerOrder agentControllerOrder = this.httpClient.execute(httpGet, new AgentControllerOrderResponseHandler());
        if (agentControllerOrder == null) {
            return;
        }

        this.applyOrder(agentControllerOrder);
    }

    private void initialize() {
        this.initializeHttpClient();
        this.initializeQuartzScheduler();
        this.initializeHqCommunication();
    }

    private void initializeHqCommunication() {
        try {
            JobDataMap jobDataMap = new JobDataMap();
            jobDataMap.put(KEY_AGENT_CONTROLLER, this);
            JobDetail job = newJob(HqCommunicationJob.class).withIdentity("hq-communication", LOGSPACE_SCHEDULER_GROUP)
                    .usingJobData(jobDataMap).build();

            Trigger trigger = newTrigger().withIdentity("hq-communication-trigger", LOGSPACE_SCHEDULER_GROUP).startNow()
                    .withSchedule(simpleSchedule().withIntervalInSeconds(this.hqCommunicationInterval).repeatForever()).build();

            this.scheduler.scheduleJob(job, trigger);
        } catch (SchedulerException e) {
            throw new AgentControllerInitializationException("Error while scheduling a Quartz job.", e);
        }
    }

    private void initializeHttpClient() {
        this.httpClient = HttpClients.createDefault();
    }

    private void initializeQuartzScheduler() {
        if (this.scheduler != null) {
            return;
        }

        if (this.isShaded()) {
            System.setProperty("org.quartz.properties", "logspace-shaded-quartz.properties");
        } else {
            System.setProperty("org.quartz.properties", "logspace-quartz.properties");
        }

        try {
            this.scheduler = new StdSchedulerFactory().getScheduler();
            this.scheduler.start();
        } catch (SchedulerException e) {
            throw new AgentControllerInitializationException("Error while creating and starting a Quartz scheduler.", e);
        }
    }

    private boolean isShaded() {
        try {
            Class.forName("io.logspace.agent.impl.shaded.quartz.simpl.SimpleThreadPool");
        } catch (ClassNotFoundException e) {
            return false;
        }
        return true;
    }

    private void sendEvents(Collection<Event> event) throws IOException, ClientProtocolException {
        HttpPut httpPut = new HttpPut(this.baseUrl + "/events/");
        httpPut.setEntity(toJsonEntity(event));

        this.httpClient.execute(httpPut, new UploadEventsResponseHandler());
    }

    private void uploadCapabilities() throws IOException {
        if (!this.modifiedAgents) {
            return;
        }

        AgentControllerCapabilities capabilities = this.getCapabilities();
        this.modifiedAgents = false;

        HttpPut httpPut = new HttpPut(this.baseUrl + "/capabilities/" + this.getId());
        httpPut.setEntity(toJSonEntity(capabilities));

        this.httpClient.execute(httpPut, new UploadCapabilitiesResponseHandler());
    }

    public static class AgentExecutionJob implements Job {

        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();

            HqAgentController agentController = (HqAgentController) jobDataMap.get(KEY_AGENT_CONTROLLER);
            String agentId = jobDataMap.getString(KEY_AGENT_ID);

            agentController.executeAgent(agentId);
        }
    }

    public static class HqCommunicationJob implements Job {

        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();

            HqAgentController agentController = (HqAgentController) jobDataMap.get(KEY_AGENT_CONTROLLER);

            agentController.callHQ();
        }
    }
}
