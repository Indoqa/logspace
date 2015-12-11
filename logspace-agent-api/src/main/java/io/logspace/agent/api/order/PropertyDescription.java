/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.api.order;

import io.logspace.agent.api.event.EventProperty;

/**
 * A description of an {@link io.logspace.agent.api.event.EventProperty EventProperty}.
 *
 * This class can be used by an {@link io.logspace.agent.api.Agent Agent} to provide additional information about his EventProperties.
 */
public class PropertyDescription implements Comparable<PropertyDescription> {

    /**
     * The ID of the {@link io.logspace.agent.api.event.EventProperty EventProperty}.
     */
    private String id;

    /**
     * The name of the {@link io.logspace.agent.api.event.EventProperty EventProperty} that is suitable for displaying in a user
     * interface.
     */
    private String name;

    /**
     * The type of the value of the {@link io.logspace.agent.api.event.EventProperty EventProperty}.
     */
    private PropertyType propertyType;

    /**
     * {@link PropertyUnit PropertyUnits} suitable for the {@link EventProperty}.
     */
    private PropertyUnit[] units;

    @Override
    public int compareTo(PropertyDescription other) {
        int nameComparison = this.getName().compareToIgnoreCase(other.getName());

        if (nameComparison != 0) {
            return nameComparison;
        }

        return this.getId().compareToIgnoreCase(other.getId());
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public PropertyType getPropertyType() {
        return this.propertyType;
    }

    public PropertyUnit[] getUnits() {
        return this.units;
    }

    /**
     * @return <code>true</code> if this {@link PropertyDescription} contains at least one {@link PropertyUnit}.
     */
    public boolean hasUnits() {
        return this.units != null && this.units.length > 0;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPropertyType(PropertyType propertyType) {
        this.propertyType = propertyType;
    }

    public void setUnits(PropertyUnit[] units) {
        this.units = units != null ? units.clone() : null;
    }

    public static class PropertyUnit {

        private String name;
        private double factor;

        public double getFactor() {
            return this.factor;
        }

        public String getName() {
            return this.name;
        }

        public void setFactor(double factor) {
            this.factor = factor;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
