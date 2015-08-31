/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.api.order;

import io.logspace.agent.api.event.Optional;

import java.util.ArrayList;
import java.util.List;

public class AgentControllerOrder {

    public static final String FIELD_COMMIT_MAX_COUNT = "commit-max-count";
    public static final String FIELD_COMMIT_MAX_SECONDS = "commit-max-seconds";
    public static final String FIELD_AGENT_ORDERS = "agent-orders";
    public static final String FIELD_ID = "id";
    public static final String FIELD_TRIGGER_TYPE = "trigger-type";
    public static final String FIELD_TRIGGER_PARAMETER = "trigger-parameter";

    private List<AgentOrder> agentOrders = new ArrayList<AgentOrder>();

    private Optional<Integer> commitMaxSeconds;
    private Optional<Integer> commitMaxCount;

    public void add(AgentOrder agentOrder) {
        this.agentOrders.add(agentOrder);
    }

    public List<AgentOrder> getAgentOrders() {
        return this.agentOrders;
    }

    public int getAgentOrdersCount() {
        if (this.agentOrders == null) {
            return 0;
        }

        return this.agentOrders.size();
    }

    public Optional<Integer> getCommitMaxCount() {
        return this.commitMaxCount;
    }

    public Optional<Integer> getCommitMaxSeconds() {
        return this.commitMaxSeconds;
    }

    public boolean hasAgentOrders() {
        return this.agentOrders != null && !this.agentOrders.isEmpty();
    }

    public void setAgentOrders(List<AgentOrder> agentOrders) {
        this.agentOrders = agentOrders;
    }

    public void setCommitMaxCount(Optional<Integer> commitMaxCount) {
        this.commitMaxCount = commitMaxCount;
    }

    public void setCommitMaxSeconds(Optional<Integer> commitMaxSeconds) {
        this.commitMaxSeconds = commitMaxSeconds;
    }
}
