/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.api;

import io.logspace.agent.api.event.Event;
import io.logspace.agent.api.order.AgentCapabilities;
import io.logspace.agent.api.order.AgentOrder;
import io.logspace.agent.api.order.TriggerType;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * Base class for {@link Agent}s. Extend it for simplified initialization (setup of {@link AgentController}) and
 * handling of {@link AgentCapabilities}.<br/>
 *
 * It will delegate the sending of {@link Event}s to the {@link AgentController}. <br/>
 * Also the {@link TriggerType}.Off will be added to the supported trigger types, to ensure derived agents can be turned
 * <strong>off</strong>.
 *
 */
public abstract class AbstractAgent implements Agent {

    private final String id;
    private final String type;

    private AgentCapabilities capabilities;
    private AgentController agentController;

    protected AbstractAgent(String id, String type, TriggerType triggerType) {
        super();

        this.id = id;
        this.type = type;
        this.updateCapabilities(triggerType);

        this.setAgentController(AgentControllerProvider.getAgentController());
    }

    private static TriggerType[] addBaseTrigger(TriggerType triggerTypes) {
        Set<TriggerType> result = new HashSet<TriggerType>();

        result.add(TriggerType.Off);
        result.add(triggerTypes);

        return result.toArray(new TriggerType[result.size()]);
    }

    @Override
    public void execute(AgentOrder agentOrder) {
        // default does nothing
    }

    @Override
    public final AgentCapabilities getCapabilities() {
        return this.capabilities;
    }

    @Override
    public final String getId() {
        return this.id;
    }

    @Override
    public final String getType() {
        return this.type;
    }

    protected final AgentController getAgentController() {
        return this.agentController;
    }

    protected String getSystem() {
        return this.agentController.getSystem();
    }

    protected final boolean isEnabled() {
        return this.agentController != null && this.agentController.isAgentEnabled(this.id);
    }

    protected final void sendEvent(Event event) {
        this.agentController.send(event);
    }

    protected final void updateCapabilities(TriggerType triggerTypes) {
        AgentCapabilities agentCapabilities = new AgentCapabilities();

        agentCapabilities.setId(this.getId());
        agentCapabilities.setType(this.getType());
        agentCapabilities.setSupportedTriggerTypes(addBaseTrigger(triggerTypes));

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
