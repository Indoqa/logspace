/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.api;

import io.logspace.agent.api.order.AgentOrder;

/**
 * A logspace.io {@link Agent} that will be triggered by a scheduler.
 */
public interface SchedulerAgent extends Agent {

    /**
     * This method will be called by a scheduler when the execution of the Agent is due.
     *
     * @param agentOrder - The {@link AgentOrder} to be executed
     */
    void execute(AgentOrder agentOrder);

}
