/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.api;

import io.logspace.agent.api.order.AgentCapabilities;
import io.logspace.agent.api.order.AgentOrder;
import io.logspace.agent.api.order.TriggerType;

public abstract class AbstractAgent implements Agent {

    private String id;
    private String type;

    private boolean enabled;

    private AgentCapabilities capabilities;

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

    @Override
    public final boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public final void setEnabled(boolean enabled) {
        this.enabled = enabled;
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
