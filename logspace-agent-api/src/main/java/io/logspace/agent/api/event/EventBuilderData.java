/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.api.event;

public class EventBuilderData {

    private final String agentId;
    private final String system;
    private final String marker;

    public EventBuilderData(String agentId, String system, String marker) {
        super();

        this.agentId = agentId;
        this.system = system;
        this.marker = marker;
    }

    public String getAgentId() {
        return this.agentId;
    }

    public String getMarker() {
        return this.marker;
    }

    public String getSystem() {
        return this.system;
    }
}
