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
import io.logspace.agent.scheduling.AgentExecutor;
import io.logspace.agent.scheduling.AgentScheduler;

import java.io.IOException;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HqAgentController extends AbstractAgentController implements AgentExecutor {

    private static final String BASE_URL_PARAMETER = "base-url";

    private static final String HQ_COMMUNICATION_INTERVAL_PARAMETER = "hq-communication-interval";
    private static final String HQ_COMMUNICATION_INTERVAL_DEFAULT_VALUE = "60";

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private boolean modifiedAgents;

    private HqClient hqClient;

    private AgentScheduler agentScheduler;

    public HqAgentController(AgentControllerDescription agentControllerDescription) {
        this.setId(agentControllerDescription.getId());

        String baseUrl = agentControllerDescription.getParameterValue(BASE_URL_PARAMETER);
        this.hqClient = new HqClient(baseUrl, this.getId());

        int hqCommunicationInterval = Integer.parseInt(agentControllerDescription.getParameterValue(
                HQ_COMMUNICATION_INTERVAL_PARAMETER, HQ_COMMUNICATION_INTERVAL_DEFAULT_VALUE));
        this.agentScheduler = new AgentScheduler(this, hqCommunicationInterval);
    }

    public static void install(String id, String baseUrl) {
        AgentControllerDescription description = AgentControllerDescription.withClass(HqAgentController.class);
        description.setId(id);
        description.addParameter(Parameter.create(BASE_URL_PARAMETER, baseUrl));

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
            this.agentScheduler.shutdown();
            this.logger.debug("Scheduler is stopped.");
        } catch (AgentControllerException acex) {
            this.logger.error("Failed to stop scheduler.", acex);
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

    private void downloadOrder() throws IOException {
        AgentControllerOrder agentControllerOrder = this.hqClient.downloadOrder();
        if (agentControllerOrder == null) {
            return;
        }

        this.agentScheduler.applyOrder(agentControllerOrder);
    }

    private void sendEvents(Collection<Event> events) throws IOException {
        this.hqClient.uploadEvents(events);
    }

    private void uploadCapabilities() throws IOException {
        if (!this.modifiedAgents) {
            return;
        }

        AgentControllerCapabilities capabilities = this.getCapabilities();
        this.modifiedAgents = false;

        this.hqClient.uploadCapabilities(capabilities);
    }
}