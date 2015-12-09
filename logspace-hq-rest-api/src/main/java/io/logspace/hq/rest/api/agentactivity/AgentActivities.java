/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.rest.api.agentactivity;

import java.util.ArrayList;
import java.util.List;

public class AgentActivities {

    private int offset;
    private int totalCount;
    private int maxHistoryValue;

    private List<AgentActivity> agentActivities = new ArrayList<>();

    public void add(AgentActivity actvitiy) {
        this.agentActivities.add(actvitiy);
    }

    public List<AgentActivity> getAgentActivities() {
        return this.agentActivities;
    }

    public int getMaxHistoryValue() {
        return this.maxHistoryValue;
    }

    public int getOffset() {
        return this.offset;
    }

    public int getTotalCount() {
        return this.totalCount;
    }

    public void setAgentActivities(List<AgentActivity> agentActivities) {
        this.agentActivities = agentActivities;
    }

    public void setMaxHistoryValue(int maxHistoryValue) {
        this.maxHistoryValue = maxHistoryValue;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
}
