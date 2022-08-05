package org.jsengine.utils;

public class Optional<T> {
    private T value_ = null;

    public Optional() {
        this(null);
    }

    public Optional(T value) {
        this.value_ = value;
    }

    public boolean hasValue() {
        return value_ != null;
    }

    public T get() {
        return value_;
    }
}
