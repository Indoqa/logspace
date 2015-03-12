/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.api.event;

/**
 * An immutable key-value pair representation an event property.
 */
public class EventProperty {

    /**
     * The key of the property.
     */
    private final String key;

    /**
     * The value of the property.
     */
    private final String value;

    public EventProperty(String key, String value) {
        if (key == null) {
            throw new NullPointerException("The key must not be null.");
        }
        if (value == null) {
            throw new NullPointerException("the value must not be null.");
        }

        this.key = key;
        this.value = value;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }

        EventProperty other = (EventProperty) obj;
        if (this.key == null) {
            if (other.key != null) {
                return false;
            }
        } else if (!this.key.equals(other.key)) {
            return false;
        }
        if (this.value == null) {
            if (other.value != null) {
                return false;
            }
        } else if (!this.value.equals(other.value)) {
            return false;
        }
        return true;
    }

    /**
     * @return The key.
     */
    public String getKey() {
        return this.key;
    }

    /**
     * @return The value.
     */
    public String getValue() {
        return this.value;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = prime + (this.key == null ? 0 : this.key.hashCode());
        return prime * result + (this.value == null ? 0 : this.value.hashCode());
    }
}
