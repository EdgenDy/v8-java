package org.jsengine.utils;

import java.util.HashSet;

public class Set<T> extends HashSet<T> {
    public Set(T... values) {
        for(T value: values)
            add(value);
    }

    public void insert(T value) {
        add(value);
    }
}
