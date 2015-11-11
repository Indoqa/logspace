/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.impl;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.logspace.agent.api.Agent;
import io.logspace.agent.api.AgentController;
import io.logspace.agent.api.AgentControllerDescription;
import io.logspace.agent.api.event.Event;
import io.logspace.agent.api.order.AgentControllerCapabilities;
import io.logspace.agent.api.util.ConsoleWriter;

/**
 * Base class for {@link AgentController}s. Initializes the system name and handles un-/registering of {@link Agent}s.
 */
public abstract class AbstractAgentController implements AgentController {

    protected static final String MARKER_PARAMETER = "marker";

    private final Map<String, Agent> agents = new ConcurrentHashMap<String, Agent>();

    private String id;
    private String system;
    private String marker;

    protected AbstractAgentController(AgentControllerDescription agentControllerDescription) {
        super();

        this.id = agentControllerDescription.getId();
        this.marker = agentControllerDescription.getParameterValue(MARKER_PARAMETER);

        this.initalizeSystem();
    }

    /**
     * @see io.logspace.agent.api.AgentController#flush()
     */
    @Override
    public void flush() {
        // default does nothing
    }

    /**
     * @see io.logspace.agent.api.AgentController#getId()
     */
    @Override
    public String getId() {
        return this.id;
    }

    /**
     * @see io.logspace.agent.api.AgentController#getMarker()
     */
    @Override
    public String getMarker() {
        return this.marker;
    }

    /**
     * @see io.logspace.agent.api.AgentController#getSystem()
     */
    @Override
    public String getSystem() {
        return this.system;
    }

    /**
     * @see io.logspace.agent.api.AgentController#isAgentEnabled(java.lang.String)
     */
    @Override
    public boolean isAgentEnabled(String agentId) {
        return true;
    }

    /**
     * @see io.logspace.agent.api.AgentController#register(io.logspace.agent.api.Agent)
     */
    @Override
    public final void register(Agent agent) {
        this.agents.put(agent.getId(), agent);

        this.onAgentRegistered(agent);
    }

    /**
     * @see io.logspace.agent.api.AgentController#send(io.logspace.agent.api.event.Event)
     */
    @Override
    public void send(Event event) {
        this.send(Collections.singleton(event));
    }

    /**
     * @see io.logspace.agent.api.AgentController#shutdown()
     */
    @Override
    public void shutdown() {
        // default does nothing
    }

    /**
     * @see io.logspace.agent.api.AgentController#unregister(io.logspace.agent.api.Agent)
     */
    @Override
    public final void unregister(Agent agent) {
        this.agents.remove(agent.getId());

        this.onAgentUnregistered(agent);
    }

    /**
     * Returns the {@link Agent} with the supplied agentId or null if there is no agent with this agentId is registered.
     *
     * @param agentId - The agentId of the registered {@link Agent}.
     * @return The registered {@link Agent} or null if no agent is registered with this agentId.
     */
    protected Agent getAgent(String agentId) {
        return this.agents.get(agentId);
    }

    /**
     * @return The agentIds of all registered {@link Agent}s.
     */
    protected Collection<String> getAgentIds() {
        return this.agents.keySet();
    }

    /**
     * @return All registered {@link Agent}s.
     */
    protected Iterable<Agent> getAgents() {
        return this.agents.values();
    }

    /**
     *
     * @return The {@link AgentControllerCapabilities} for each registered {@link Agent}.
     */
    protected AgentControllerCapabilities getCapabilities() {
        AgentControllerCapabilities result = new AgentControllerCapabilities();

        result.setId(this.getId());
        result.setSystem(this.getSystem());

        for (Agent eachAgent : this.getAgents()) {
            result.add(eachAgent.getCapabilities());
        }

        return result;
    }

    /**
     * Called when an {@link Agent} has registered itself with this {@link AgentController}.
     *
     * @param agent The registered agent.
     */
    protected void onAgentRegistered(Agent agent) {
        // default does nothing
    }

    /**
     * Called when an {@link Agent} has unregistered itself from this {@link AgentController}.
     *
     * @param agent The unregistered agent.
     */
    protected void onAgentUnregistered(Agent agent) {
        // default does nothing
    }

    protected void setMarker(String marker) {
        this.marker = marker;
    }

    private void initalizeSystem() {
        try {
            this.system = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            ConsoleWriter.writeSystem("Failed to retrieve system name: " + e);
            this.system = "UNKNOWN";
        }
    }
}
