/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.core.api;

import io.logspace.agent.api.order.PropertyDescription;

import java.util.ArrayList;
import java.util.List;

public class AgentDescription {

    private String globalId;
    private String name;
    private String space;
    private String system;

    private List<PropertyDescription> propertyDescriptions = new ArrayList<>();

    public void addPropertyDescription(PropertyDescription propertyDescription) {
        this.propertyDescriptions.add(propertyDescription);
    }

    public String getGlobalId() {
        return this.globalId;
    }

    public String getName() {
        return this.name;
    }

    public List<PropertyDescription> getPropertyDescriptions() {
        return this.propertyDescriptions;
    }

    public String getSpace() {
        return this.space;
    }

    public String getSystem() {
        return this.system;
    }

    public void setGlobalId(String globalId) {
        this.globalId = globalId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPropertyDescriptions(List<PropertyDescription> propertyDescriptions) {
        this.propertyDescriptions = propertyDescriptions;
    }

    public void setSpace(String space) {
        this.space = space;
    }

    public void setSystem(String system) {
        this.system = system;
    }
}