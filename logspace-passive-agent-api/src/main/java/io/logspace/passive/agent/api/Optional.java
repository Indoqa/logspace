package io.logspace.passive.agent.api;

import java.util.NoSuchElementException;
import java.util.Objects;

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

}
