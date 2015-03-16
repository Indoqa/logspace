/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.api.order;

public class AgentOrder {

    private String id;

    private TriggerType triggerType;

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