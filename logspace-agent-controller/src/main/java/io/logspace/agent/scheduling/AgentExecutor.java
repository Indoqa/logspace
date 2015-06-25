/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.scheduling;

import io.logspace.agent.api.order.AgentOrder;

/**
 * The agent executor is responsible for the time-based execution of its methods.
 */
public interface AgentExecutor {

    /**
     * Time-based execution of the agent with its order.
     *
     * @param agentOrder - The {@link AgentOrder} of the {@link Agent} to execute.
     */
    void executeScheduledAgent(AgentOrder agentOrder);

    /**
     * Time-based execution of updates.
     */
    void update();

}
