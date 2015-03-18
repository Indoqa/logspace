/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.api.event;

import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * A Java 7 implementation of Optional that can be replaced by the Java 8 equivalent.
 *
 * @see http://docs.oracle.com/javase/8/docs/api/java/util/Optional.html
 */
public class Optional<T> {

    private static final Optional<?> EMPTY = new Optional<Object>();

    private final T value;

    private Optional() {
        this.value = null;
    }

    private Optional(T value) {
        if (value == null) {
            throw new NullPointerException("The value must not be null.");
        }

        this.value = value;
    }

    @SuppressWarnings("unchecked")
    public static <T> Optional<T> empty() {
        return (Optional<T>) EMPTY;
    }

    public static <T> Optional<T> of(T value) {
        return new Optional<T>(value);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Optional)) {
            return false;
        }

        Optional<?> other = (Optional<?>) obj;
        return Objects.equals(this.value, other.value);
    }

    public T get() {
        if (this.value == null) {
            throw new NoSuchElementException("No value present");
        }
        return this.value;
    }

    @Override
    public int hashCode() {
        if (this.value == null) {
            return 0;
        }
        return this.value.hashCode();
    }

    public boolean isPresent() {
        return this.value != null;
    }

    public T orElse(T other) {
        if (this.value == null) {
            return other;
        }
        return this.value;
    }

    @Override
    public String toString() {
        return "[Optional of '" + this.value + "']";
    }

}
