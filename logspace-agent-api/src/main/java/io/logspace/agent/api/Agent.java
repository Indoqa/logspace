/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.api;

import io.logspace.agent.api.order.AgentCapabilities;

/**
 * An Agent is responsible for packaging collected information into {@link io.logspace.agent.api.event.Event Events}.
 *
 */
public interface Agent {

    /**
     *
     * @return {@link AgentCapabilities} of this agent.
     */
    AgentCapabilities getCapabilities();

    /**
     * @return The unique id of this agent.
     */
    String getId();

    /**
     * The type describes the intention of this agent.
     *
     * @return The type of this agent.
     */
    String getType();

}
