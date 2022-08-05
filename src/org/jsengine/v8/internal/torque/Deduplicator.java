package org.jsengine.v8.internal.torque;

import org.jsengine.utils.UnorderedSet;

public class Deduplicator<T> {
    private UnorderedSet<T> storage_ = new UnorderedSet<T>();
    public T add(T x) {
        storage_.add(x);
        return x;
    }
}
