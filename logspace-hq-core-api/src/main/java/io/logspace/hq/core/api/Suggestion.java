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
import java.util.Set;
import java.util.TreeSet;

public class Suggestion {

    private Set<String> spaces = new TreeSet<>();
    private Set<String> systems = new TreeSet<>();
    private Set<String> propertyNames = new TreeSet<>();

    private List<AgentDescription> agentDescriptions = new ArrayList<>();

    public void addAgentDescription(AgentDescription agentDescription) {
        this.agentDescriptions.add(agentDescription);

        this.spaces.add(agentDescription.getSpace());
        this.systems.add(agentDescription.getSystem());

        for (PropertyDescription eachPropertyDescription : agentDescription.getPropertyDescriptions()) {
            this.propertyNames.add(eachPropertyDescription.getName());
        }
    }

    public List<AgentDescription> getAgentDescriptions() {
        return this.agentDescriptions;
    }

    public Set<String> getPropertyNames() {
        return this.propertyNames;
    }

    public Set<String> getSpaces() {
        return this.spaces;
    }

    public Set<String> getSystems() {
        return this.systems;
    }

    public void setAgentDescriptions(List<AgentDescription> agentDescriptions) {
        this.agentDescriptions = agentDescriptions;
    }

    public void setPropertyNames(Set<String> propertyNames) {
        this.propertyNames = propertyNames;
    }

    public void setSpaces(Set<String> spaces) {
        this.spaces = spaces;
    }

    public void setSystems(Set<String> systems) {
        this.systems = systems;
    }

}