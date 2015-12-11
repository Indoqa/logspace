/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.api.order;

/**
 * The {@link AgentOrder} describes execution parameters for a single {@link io.logspace.agent.api.Agent Agent} and determines, if and
 * when an Agent is to be executed.<br>
 * <br>
 * For {@link io.logspace.agent.api.SchedulerAgent SchedulerAgents} an AgentOrder is required in order to be executed by the scheduler.
 * <br>
 *
 * For {@link io.logspace.agent.api.ApplicationAgent ApplicationAgents} honoring the AgentOrder is up to the actual implementation of
 * {@link AgentControllerOrder} in use and the Agent itself. Certain implementations may allow the execution of ApplicationAgents
 * without orders or even when an order with {@link TriggerType#Off} is present.
 *
 * @see AgentControllerOrder
 */
public class AgentOrder {

    /**
     * The ID of this order. This is also the ID of the {@link io.logspace.agent.api.Agent Agent} this order is to be applied to.
     */
    private String id;

    /**
     * The type of {@link TriggerType} to be used for the {@link io.logspace.agent.api.Agent Agent}.
     */
    private TriggerType triggerType;

    /**
     * An optional parameter for the selected trigger, e.g. a cron expression.
     */
    private String triggerParameter;

    public String getId() {
        return this.id;
    }

    public String getTriggerParameter() {
        return this.triggerParameter;
    }

    public TriggerType getTriggerType() {
        return this.triggerType;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTriggerParameter(String triggerParameter) {
        this.triggerParameter = triggerParameter;
    }

    public void setTriggerType(TriggerType triggerType) {
        this.triggerType = triggerType;
    }
}
