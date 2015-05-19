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

    private Set<FacetValue> spaces = new TreeSet<>();
    private Set<FacetValue> systems = new TreeSet<>();
    private Set<FacetValue> propertyNames = new TreeSet<>();

    private List<AgentDescription> agentDescriptions = new ArrayList<>();

    public void addAgentDescription(AgentDescription agentDescription) {
        this.agentDescriptions.add(agentDescription);

        this.spaces.add(new FacetValue(agentDescription.getSpace()));
        this.systems.add(new FacetValue(agentDescription.getSystem()));

        for (PropertyDescription eachPropertyDescription : agentDescription.getPropertyDescriptions()) {
            this.propertyNames.add(new FacetValue(eachPropertyDescription.getId(), eachPropertyDescription.getName()));
        }
    }

    public List<AgentDescription> getAgentDescriptions() {
        return this.agentDescriptions;
    }

    public Set<FacetValue> getPropertyNames() {
        return this.propertyNames;
    }

    public Set<FacetValue> getSpaces() {
        return this.spaces;
    }

    public Set<FacetValue> getSystems() {
        return this.systems;
    }

    public void setAgentDescriptions(List<AgentDescription> agentDescriptions) {
        this.agentDescriptions = agentDescriptions;
    }

    public void setPropertyNames(Set<FacetValue> propertyNames) {
        this.propertyNames = propertyNames;
    }

    public void setSpaces(Set<FacetValue> spaces) {
        this.spaces = spaces;
    }

    public void setSystems(Set<FacetValue> systems) {
        this.systems = systems;
    }

    public static class FacetValue implements Comparable<FacetValue> {

        private final String id;
        private final String name;

        public FacetValue(String id) {
            this(id, id);
        }

        public FacetValue(String id, String name) {
            super();
            this.id = id;
            this.name = name;
        }

        @Override
        public int compareTo(FacetValue o) {
            int comparison = this.name.compareTo(o.name);
            if (comparison != 0) {
                return comparison;
            }

            return this.id.compareTo(o.id);
        }

        public String getId() {
            return this.id;
        }

        public String getName() {
            return this.name;
        }
    }
}