/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.api;

import io.logspace.agent.api.event.Event;

import java.util.Collection;

/**
 * The AgentController is responsible for un-/registering {@link Agent}s and sending of {@link Event}s.
 *
 */
public interface AgentController {

    /**
     * Flushes queued events.
     */
    void flush();

    /**
     * @return The Id of the AgentController
     */
    String getId();

    /**
     * @return The systems name.
     */
    String getSystem();

    boolean isAgentEnabled(String agentId);

    /**
     * Registers an agent with this AgentController.
     *
     * @param agent - The agent to be registered.
     */
    void register(Agent agent);

    /**
     * Sends multiple {@link Event}s.
     *
     * @param events - {@link Event}s to send.
     */
    void send(Collection<Event> events);

    /**
     * Sends a single {@link Event}.
     *
     * @param event - The {@link Event} to send.
     */
    void send(Event event);

    /**
     * Called on shutdown to release resources.
     */
    void shutdown();

    /**
     * Unregisters an agent with this AgentController.
     *
     * @param agent - The {@link Agent} to be unregistered.
     */
    void unregister(Agent agent);

}
