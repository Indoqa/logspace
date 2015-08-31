/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.api;

import static io.logspace.agent.api.order.TriggerType.Off;
import static io.logspace.agent.api.order.TriggerType.Scheduler;
import io.logspace.agent.api.order.TriggerType;

public abstract class AbstractSchedulerAgent extends AbstractAgent implements SchedulerAgent {

    private static final TriggerType[] TRIGGER_TYPES = new TriggerType[] {Off, Scheduler};

    protected AbstractSchedulerAgent(String id, String type) {
        super(id, type, TRIGGER_TYPES);
    }
}
