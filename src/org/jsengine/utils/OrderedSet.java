package org.jsengine.utils;

import java.util.LinkedHashSet;

public class OrderedSet<T> extends LinkedHashSet<T>  {
    public OrderedSet() {
        super();
    }

    public T begin() {
        if(size() > 0)
            return iterator().next();
        throw new RuntimeException("accessing first element of Set on an empty set.");
    }
}
