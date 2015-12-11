/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.api;

import java.util.Collection;

import io.logspace.agent.api.event.Event;

/**
 * The AgentController is responsible for un-/registering {@link io.logspace.agent.api.AgentAgent Agents} and handling of {@link Event
 * Events}.
 */
public interface AgentController {

    /**
     * Flushes queued events.
     */
    void flush();

    /**
     * @return The ID of the AgentController
     */
    String getId();

    /**
     * @return The current marker. This may be <code>null</code>.
     */
    String getMarker();

    /**
     * @return The system's name.
     */
    String getSystem();

    /**
     * Determines whether the {@link io.logspace.agent.api.Agent Agent} with the given <code>agentId</code> is enabled or not.<br>
     * If no Agent is currently registered with AgentController this method should return <code>false</code>.
     *
     * @param agentId The ID of the Agent to check.
     *
     * @return <code>true</code> if an Agent with the given <code>agentId</code> is registered and enabled.
     */
    boolean isAgentEnabled(String agentId);

    /**
     * Registers an {@link io.logspace.agent.api.Agent Agent} with this AgentController.<br>
     * Registering an Agent more than once has no effect.
     *
     * @param agent - The Agent to be registered.
     */
    void register(Agent agent);

    /**
     * Sends multiple {@link Event Events}.
     *
     * @param events - The Events to be sent.
     */
    void send(Collection<Event> events);

    /**
     * Sends a single {@link Event}.
     *
     * @param event - The Event to be sent.
     */
    void send(Event event);

    /**
     * Called on shutdown to release resources.
     */
    void shutdown();

    /**
     * Unregisters an {@link io.logspace.agent.api.Agent Agent} with this AgentController.
     *
     * @param agent - The Agent to be unregistered.
     */
    void unregister(Agent agent);

}
