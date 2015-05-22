/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.os;

import io.logspace.agent.api.AbstractAgent;
import io.logspace.agent.api.order.TriggerType;

public abstract class AbstractOsAgent extends AbstractAgent {

    protected AbstractOsAgent(String type) {
        super(type, type, TriggerType.Off, TriggerType.Cron);
    }

    public final void execute() {
        this.execute(null);
    }
}