/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.api.order;

import java.util.ArrayList;
import java.util.List;

import io.logspace.agent.api.Agent;
import io.logspace.agent.api.AgentController;

/**
 * This class describes the capabilities of an {@link AgentController} and its currently registered {@link Agent Agents}.
 *
 * @see AgentCapabilities
 */
public class AgentControllerCapabilities {

    public static final String FIELD_ID = "id";
    public static final String FIELD_SYSTEM = "system";
    public static final String FIELD_SPACE = "space";
    public static final String FIELD_TYPE = "type";
    public static final String FIELD_TRIGGER_TYPES = "trigger-types";
    public static final String FIELD_AGENT_CAPABILITIES = "agent-capabilities";

    public static final String FIELD_PROPERTY_DESCRIPTIONS = "property-descriptions";
    public static final String FIELD_PROPERTY_NAME = "name";
    public static final String FIELD_PROPERTY_TYPE = "type";
    public static final String FIELD_PROPERTY_UNITS = "units";
    public static final String FIELD_PROPERTY_FACTORS = "factors";

    /**
     * The ID of the {@link AgentController}
     */
    private String id;

    /**
     * The space of the {@link AgentController}
     */
    private String space;

    /**
     * The system of the {@link AgentController}
     */
    private String system;

    private List<AgentCapabilities> agentCapabilities = new ArrayList<AgentCapabilities>();

    public void add(AgentCapabilities capabilities) {
        this.agentCapabilities.add(capabilities);
    }

    public List<AgentCapabilities> getAgentCapabilities() {
        return this.agentCapabilities;
    }

    public int getAgentCapabilitiesCount() {
        return this.agentCapabilities.size();
    }

    public String getId() {
        return this.id;
    }

    public String getSpace() {
        return this.space;
    }

    public String getSystem() {
        return this.system;
    }

    /**
     * @return <code>true</code> if this {@link AgentControllerCapabilities} contains at least one {@link AgentCapabilities}.
     */
    public boolean hasAgentCapabilities() {
        return this.agentCapabilities != null && !this.agentCapabilities.isEmpty();
    }

    public void setAgentCapabilities(List<AgentCapabilities> agentCapabilities) {
        this.agentCapabilities = agentCapabilities;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSpace(String space) {
        this.space = space;
    }

    public void setSystem(String system) {
        this.system = system;
    }
}
