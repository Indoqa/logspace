/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.api;

import static io.logspace.agent.api.order.TriggerType.*;

import io.logspace.agent.api.order.TriggerType;

/**
 * Base class for all {@link Agent Agents} that will be triggered by an application.
 */
public abstract class AbstractApplicationAgent extends AbstractAgent implements ApplicationAgent {

    private static final TriggerType[] TRIGGER_TYPES = new TriggerType[] {Off, Application};

    protected AbstractApplicationAgent(String id, String type) {
        super(id, type, TRIGGER_TYPES);
    }
}
