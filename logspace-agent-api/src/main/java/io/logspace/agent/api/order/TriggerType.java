/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.api.order;

/**
 * Defines the types of triggers that will cause the execution of an {@link io.logspace.agent.api.Agent Agent}.
 *
 * @see AgentOrder
 */
public enum TriggerType {

    /**
     * The {@link io.logspace.agent.api.Agent Agent} is to be executed by the control flow of the application.
     */
    Application,

    /**
     * The {@link io.logspace.agent.api.Agent Agent} is to be executed by the scheduler of its {@link AgentControllerOrder}.
     */
    Scheduler,

    /**
     * The {@link io.logspace.agent.api.Agent Agent} should not be executed. Note that implementations of Agent and
     * {@link io.logspace.agent.api.AgentController AgentController} can ignore this TriggerType and still execute an Agent.
     */
    Off;

    public static TriggerType get(String name) {
        for (TriggerType eachTriggerType : TriggerType.values()) {
            if (eachTriggerType.name().equalsIgnoreCase(name)) {
                return eachTriggerType;
            }
        }

        return null;
    }
}
