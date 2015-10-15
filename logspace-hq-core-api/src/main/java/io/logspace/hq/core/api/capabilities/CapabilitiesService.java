/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.core.api.capabilities;

import io.logspace.agent.api.order.AgentControllerCapabilities;
import io.logspace.hq.rest.api.suggestion.AgentDescription;

import java.io.IOException;

/**
 * This service is responsible for storing information about {@link io.logspace.agent.api.Agent Agents}.
 */
public interface CapabilitiesService {

    /**
     * Retrieve the {@link AgentDescription} for the globalAgentId.
     *
     * @param globalAgentId - The globalAgentId to retrieve the {@link AgentDescription} for.
     * @return The {@link AgentDescription} for the supplied globalAgentId.
     */
    AgentDescription getAgentDescription(String globalAgentId);

    /**
     * Retrieve the agentId for the globalAgentId.
     *
     * @param globalAgentId The globalAgentId to retrieve the agentId for.
     * @return The agentId for the globalAgentId.
     */
    String getAgentId(String globalAgentId);

    /**
     * Return the globalAgentId for the supplied unique triple.
     *
     * @param space - The space of the agent.
     * @param system - The system of the agent.
     * @param agentId - The agentId of the agent.
     * @return The globaleAgentId for the unique triple (space, system, agentId).
     */
    String getGlobalAgentId(String space, String system, String agentId);

    /**
     * Store the supplied {@link AgentControllerCapabilities}
     *
     * @param capabilities - The {@link AgentControllerCapabilities} to store.
     *
     * @throws IOException If an error occurs on storing the {@link AgentControllerCapabilities}.
     */
    void save(AgentControllerCapabilities capabilities) throws IOException;

}
