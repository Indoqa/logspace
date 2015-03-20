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

public class AgentControllerCapabilities {

    public static final String FIELD_ID = "id";
    public static final String FIELD_TYPE = "type";
    public static final String FIELD_TRIGGER_TYPES = "trigger-types";
    public static final String FIELD_AGENT_CAPABILITIES = "agent-capabilities";

    private String id;

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

    public void setAgentCapabilities(List<AgentCapabilities> agentCapabilities) {
        this.agentCapabilities = agentCapabilities;
    }

    public void setId(String id) {
        this.id = id;
    }
}