/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.api.order;

/**
 * This class describes the capabilities of an {@link io.logspace.agent.api.Agent Agent}.
 */
public class AgentCapabilities {

    /**
     * The ID of the {@link io.logspace.agent.api.Agent Agent}.
     */
    private String id;

    /**
     * The type of the {@link io.logspace.agent.api.Agent Agent}.
     */
    private String type;

    /**
     * The {@link TriggerType TriggerTypes} the {@link io.logspace.agent.api.Agent Agent} supports.
     */
    private TriggerType[] supportedTriggerTypes;

    /**
     * The descriptions of the {@link io.logspace.agent.api.Agent Agent's} {@link io.logspace.agent.api.event.EventProperty
     * EventProperties}.
     */
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

    /**
     * @return <code>true</code> if this {@link AgentCapabilities} contains at least one {@link PropertyDescription}.
     */
    public boolean hasPropertyDescriptions() {
        return this.propertyDescriptions != null && this.propertyDescriptions.length > 0;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPropertyDescriptions(PropertyDescription[] propertyDescriptions) {
        this.propertyDescriptions = propertyDescriptions != null ? propertyDescriptions.clone() : null;
    }

    public void setSupportedTriggerTypes(TriggerType... supportedTriggerTypes) {
        this.supportedTriggerTypes = supportedTriggerTypes;
    }

    public void setType(String type) {
        this.type = type;
    }
}
