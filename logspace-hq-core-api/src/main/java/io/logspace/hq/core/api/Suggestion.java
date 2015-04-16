/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.core.api;

import java.util.ArrayList;
import java.util.List;

public class Suggestion {

    private List<String> spaces = new ArrayList<>();
    private List<String> agentIds = new ArrayList<>();
    private List<String> propertyNames = new ArrayList<>();

    public void addAgentId(String agentId) {
        this.agentIds.add(agentId);
    }

    public void addPropertyName(String propertyName) {
        this.propertyNames.add(propertyName);
    }

    public void addSpace(String space) {
        this.spaces.add(space);
    }

    public List<String> getAgentIds() {
        return this.agentIds;
    }

    public List<String> getPropertyNames() {
        return this.propertyNames;
    }

    public List<String> getSpaces() {
        return this.spaces;
    }

    public void setAgentIds(List<String> agentIds) {
        this.agentIds = agentIds;
    }

    public void setPropertyNames(List<String> propertyNames) {
        this.propertyNames = propertyNames;
    }

    public void setSpaces(List<String> spaces) {
        this.spaces = spaces;
    }
}