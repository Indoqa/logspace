/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.api.event;

import java.text.MessageFormat;

/**
 * Abstract base class for implementing an {@link EventProperty} allowing type-safe property values using generics.
 *
 * @param <T> The type of the value.
 */
public abstract class AbstractEventProperty<T> implements EventProperty<T> {

    /**
     * The key of the property.
     */
    private final String key;

    /**
     * The value of the property
     */
    private final T value;

    protected AbstractEventProperty(String key, T value) {
        if (key == null) {
            throw new IllegalArgumentException("The key must not be null.");
        }

        if (value == null) {
            throw new IllegalArgumentException("The value must not be null.");
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

        EventProperty<?> other = (EventProperty<?>) obj;
        if (this.getKey() == null) {
            if (other.getKey() != null) {
                return false;
            }
        } else if (!this.getKey().equals(other.getKey())) {
            return false;
        }

        if (this.getValue() == null) {
            if (other.getValue() != null) {
                return false;
            }
        } else if (!this.getValue().equals(other.getValue())) {
            return false;
        }

        return true;
    }

    @Override
    public final String getKey() {
        return this.key;
    }

    @Override
    public T getValue() {
        return this.value;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = prime + (this.key == null ? 0 : this.key.hashCode());
        return prime * result + (this.getValue() == null ? 0 : this.getValue().hashCode());
    }

    @Override
    public String toString() {
        return MessageFormat.format("[{0} = {1}]", this.key, this.value);
    }
}
