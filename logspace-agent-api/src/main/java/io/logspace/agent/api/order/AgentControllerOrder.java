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

public class AgentControllerOrder {

    private List<AgentOrder> agentOrders = new ArrayList<AgentOrder>();

    private int commitMaxSeconds;
    private int commitMaxCount;

    public List<AgentOrder> getAgentOrders() {
        return this.agentOrders;
    }

    public int getCommitMaxCount() {
        return this.commitMaxCount;
    }

    public int getCommitMaxSeconds() {
        return this.commitMaxSeconds;
    }

    public void setAgentOrders(List<AgentOrder> agentOrders) {
        this.agentOrders = agentOrders;
    }

    public void setCommitMaxCount(int commitMaxCount) {
        this.commitMaxCount = commitMaxCount;
    }

    public void setCommitMaxSeconds(int commitMaxSeconds) {
        this.commitMaxSeconds = commitMaxSeconds;
    }
}