/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.rest.api.agentactivity;

public class AgentActivity {

    private String globalAgentId;
    private int eventCount;
    private int[] history;

    public static AgentActivity create(String globalAgentId, int eventCount) {
        AgentActivity result = new AgentActivity();

        result.setGlobalAgentId(globalAgentId);
        result.setEventCount(eventCount);

        return result;
    }

    public int getEventCount() {
        return this.eventCount;
    }

    public String getGlobalAgentId() {
        return this.globalAgentId;
    }

    public int[] getHistory() {
        return this.history;
    }

    public void setEventCount(int eventCount) {
        this.eventCount = eventCount;
    }

    public void setGlobalAgentId(String globalAgentId) {
        this.globalAgentId = globalAgentId;
    }

    public void setHistory(int[] history) {
        this.history = history;
    }
}
