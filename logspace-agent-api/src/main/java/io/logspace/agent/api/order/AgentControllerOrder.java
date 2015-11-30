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
import io.logspace.agent.api.event.Event;

/**
 * The {@link AgentControllerOrder} contains configuration parameters for controlling the commit behavior of an {@link AgentController}
 * as well as the {@link AgentOrder orders} for the individual {@link Agent Agents} registered with the controller.
 */
public class AgentControllerOrder {

    public static final String FIELD_COMMIT_MAX_COUNT = "commit-max-count";
    public static final String FIELD_COMMIT_MAX_SECONDS = "commit-max-seconds";
    public static final String FIELD_AGENT_ORDERS = "agent-orders";
    public static final String FIELD_ID = "id";
    public static final String FIELD_TRIGGER_TYPE = "trigger-type";
    public static final String FIELD_TRIGGER_PARAMETER = "trigger-parameter";

    /**
     * The individual {@link AgentOrder AgentOrders} in this {@link AgentControllerOrder}
     */
    private List<AgentOrder> agentOrders = new ArrayList<AgentOrder>();

    /**
     * The maximum amount of time after which collected {@link Event Events} should be committed.
     */
    private Integer commitMaxSeconds;

    /**
     * Adds the given <code>agentOrder</code> to this {@link AgentControllerOrder}.<br>
     * <br>
     * This method will not remove an already existing order for the same {@link Agent}.
     *
     * @param agentOrder The {@link AgentOrder} to add.
     */
    public void add(AgentOrder agentOrder) {
        this.agentOrders.add(agentOrder);
    }

    /**
     * Retrieves an {@link AgentOrder} for the given <code>agentId</code> if this {@link AgentControllerOrder} contains such an order.
     *
     * @param agentId The ID of the {@link Agent} to find a matching AgentOrder for.
     * @return The first AgentOrder that matches the given <code>id</code> or <code>null</code> if no such AgentOrder exists.
     */
    public AgentOrder getAgentOrder(String agentId) {
        for (AgentOrder eachAgentOrder : this.agentOrders) {
            if (eachAgentOrder.getId().equals(agentId)) {
                return eachAgentOrder;
            }
        }

        return null;
    }

    public List<AgentOrder> getAgentOrders() {
        return this.agentOrders;
    }

    /**
     * @return The number of {@link AgentOrder} in this {@link AgentControllerOrder}.
     */
    public int getAgentOrdersCount() {
        if (this.agentOrders == null) {
            return 0;
        }

        return this.agentOrders.size();
    }

    public Integer getCommitMaxSeconds() {
        return this.commitMaxSeconds;
    }

    /**
     * @return <code>true</code> if this {@link AgentControllerOrder} contains at least one {@link AgentOrder}.
     */
    public boolean hasAgentOrders() {
        return this.agentOrders != null && !this.agentOrders.isEmpty();
    }

    public void setAgentOrders(List<AgentOrder> agentOrders) {
        this.agentOrders = agentOrders;
    }

    public void setCommitMaxSeconds(Integer commitMaxSeconds) {
        this.commitMaxSeconds = commitMaxSeconds;
    }
}
