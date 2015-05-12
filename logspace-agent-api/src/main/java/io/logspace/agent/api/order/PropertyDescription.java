/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.api.order;

import java.util.HashMap;
import java.util.Map;

public class PropertyDescription {

    private String id;
    private String name;

    private PropertyType propertyType;
    private PropertyUnit[] units;

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
        this.units = units;
    }

    public static enum PropertyType {

        BOOLEAN, DATE, INTEGER, LONG, FLOAT, DOUBLE, STRING;

        private static final Map<String, PropertyType> PROPERTY_TYPES = new HashMap<String, PropertyType>();

        static {
            for (PropertyType eachValue : PropertyType.values()) {
                PROPERTY_TYPES.put(eachValue.name().toLowerCase(), eachValue);
            }
        }

        public static PropertyType get(String name) {
            if (name == null) {
                return null;
            }

            return PROPERTY_TYPES.get(name.toLowerCase());
        }
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
