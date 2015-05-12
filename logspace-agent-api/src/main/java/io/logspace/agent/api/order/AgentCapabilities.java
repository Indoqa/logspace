/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.api.order;

public class AgentCapabilities {

    private String id;

    private String type;

    private TriggerType[] supportedTriggerTypes;

    private PropertyDescription[] propertyDescriptions;

    public String getId() {
        return this.id;
    }

    public PropertyDescription[] getPropertyDescriptions() {
        return this.propertyDescriptions;
    }

    public TriggerType[] getSupportedTriggerTypes() {
        return this.supportedTriggerTypes;
    }

    public String getType() {
        return this.type;
    }

    public boolean hasPropertyDescriptions() {
        return this.propertyDescriptions != null && this.propertyDescriptions.length > 0;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPropertyDescriptions(PropertyDescription[] propertyDescriptions) {
        this.propertyDescriptions = propertyDescriptions;
    }

    public void setSupportedTriggerTypes(TriggerType... supportedTriggerTypes) {
        this.supportedTriggerTypes = supportedTriggerTypes;
    }

    public void setType(String type) {
        this.type = type;
    }
}