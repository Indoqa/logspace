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

public abstract class AbstractAgent implements Agent {

    private String id;
    private String type;

    private AgentCapabilities capabilities;
    private AgentController agentController;

    protected AbstractAgent() {
        super();
    }

    protected AbstractAgent(String id, String type, TriggerType... triggerType) {
        super();

        this.id = id;
        this.type = type;

        this.updateCapabilities(triggerType);
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

    protected final boolean isEnabled() {
        return this.agentController != null && this.agentController.isAgentEnabled(this.id);
    }

    protected final void sendEvent(Event event) {
        this.agentController.send(event);
    }

    protected final void setAgentController(AgentController agentController) {
        if (this.agentController != null) {
            this.agentController.unregister(this);
        }

        this.agentController = agentController;

        if (this.agentController != null) {
            this.agentController.register(this);
        }
    }

    protected final void setId(String id) {
        this.id = id;
    }

    protected final void setType(String type) {
        this.type = type;
    }

    protected final void updateCapabilities(TriggerType... triggerTypes) {
        AgentCapabilities agentCapabilities = new AgentCapabilities();

        agentCapabilities.setId(this.getId());
        agentCapabilities.setType(this.getType());
        agentCapabilities.setSupportedTriggerTypes(triggerTypes);

        this.capabilities = agentCapabilities;
    }
}
