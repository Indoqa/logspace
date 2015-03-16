/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.api;

import io.logspace.agent.api.order.AgentOrder;

public abstract class AbstractAgent implements Agent {

    private String id;
    private boolean enabled;

    @Override
    public void execute(AgentOrder agentOrder) {
        // default does nothing
    }

    @Override
    public final String getId() {
        return this.id;
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
}
