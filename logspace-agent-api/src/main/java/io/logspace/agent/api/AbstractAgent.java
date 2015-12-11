/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.api;

import io.logspace.agent.api.event.Event;
import io.logspace.agent.api.event.EventBuilderData;
import io.logspace.agent.api.order.AgentCapabilities;
import io.logspace.agent.api.order.TriggerType;

/**
 * Base class for {@link Agent Agents} implementing {@link AgentController} and {@link AgentCapabilities} handling.<br>
 * <br>
 * Concrete Agent implementations should not extend this class directly, but one of its child classes instead.
 */
abstract class AbstractAgent implements Agent {

    /**
     * The ID of this Agent.
     */
    private final String id;

    /**
     * The type of this Agent.
     */
    private final String type;

    /**
     * The {@link AgentCapabilities} of this Agent.
     */
    private AgentCapabilities capabilities;

    /**
     * The {@link AgentController} this Agent is registered with.
     */
    private AgentController agentController;

    /**
     * Create a new {@link AbstractAgent} with the given values.<br>
     * <br>
     * This constructor will automatically update the Agents {@link AgentCapabilities capabilities} and set the {@link AgentController}
     * by obtaining it from the {@link AgentControllerProvider}.
     *
     * @param id The ID of this Agent
     * @param type The type of this Agent
     * @param triggerTypes The {@link TriggerType} this Agent will support.
     */
    protected AbstractAgent(String id, String type, TriggerType[] triggerTypes) {
        super();

        this.id = id;
        this.type = type;

        this.updateCapabilities(triggerTypes);
        this.setAgentController(AgentControllerProvider.getAgentController());
    }

    /**
     * @see io.logspace.agent.api.Agent#getCapabilities()
     */
    @Override
    public final AgentCapabilities getCapabilities() {
        return this.capabilities;
    }

    /**
     * @see io.logspace.agent.api.Agent#getId()
     */
    @Override
    public final String getId() {
        return this.id;
    }

    /**
     * @see io.logspace.agent.api.Agent#getType()
     */
    @Override
    public final String getType() {
        return this.type;
    }

    /**
     * @return The {@link AgentController} this Agent is currently registered with.
     */
    protected final AgentController getAgentController() {
        return this.agentController;
    }

    /**
     * @return The data necessary for creating a new {@link io.logspace.agent.api.event.AbstractEventBuilder AbstractEventBuilder} .
     */
    protected EventBuilderData getEventBuilderData() {
        return new EventBuilderData(this.getId(), this.agentController.getSystem(), this.agentController.getMarker());
    }

    /**
     * An {@link Agent} is <strong>enabled</strong>, if it is registered with an {@link AgentController} and this
     * {@link AgentController} considers this Agent to be <code>enabled</code>.
     *
     * @see AgentController#isAgentEnabled(String)
     *
     * @return Whether this {@link Agent} is currently enabled or not.
     */
    protected final boolean isEnabled() {
        return this.agentController != null && this.agentController.isAgentEnabled(this.id);
    }

    /**
     * Forward the given {@link Event} to this Agent's {@link AgentController}.<br>
     * <br>
     * If this Agent is not currently registered with an {@link AgentController}, this method will throw a
     * {@link AgentControllerException}.
     *
     * @param event The {@link Event} to be sent.
     */
    protected final void sendEvent(Event event) {
        if (this.agentController == null) {
            throw new AgentControllerException("This Agent has no AgentController.");
        }

        this.agentController.send(event);
    }

    /**
     * Update this Agent's {@link AgentCapabilities} with the given {@link TriggerType TriggerTypes}.
     *
     * @param triggerTypes The {@link TriggerType TriggerTypes} to be set.
     */
    protected final void updateCapabilities(TriggerType[] triggerTypes) {
        AgentCapabilities agentCapabilities = new AgentCapabilities();

        agentCapabilities.setId(this.getId());
        agentCapabilities.setType(this.getType());
        agentCapabilities.setSupportedTriggerTypes(triggerTypes);

        this.capabilities = agentCapabilities;
    }

    private void setAgentController(AgentController agentController) {
        if (this.agentController != null) {
            this.agentController.unregister(this);
        }

        this.agentController = agentController;

        if (this.agentController != null) {
            this.agentController.register(this);
        }
    }
}
