package org.jsengine.v8.internal.torque;

import java.util.HashMap;

public class SpecializationMap<T> {
    public static class Map<T> extends HashMap<TypeVector, T> {
        public Map() {
            super();
        }
    }
    private Map<T> specializations_ = new Map<T>();

    public SpecializationMap() {

    }

    public void add(TypeVector type_arguments, T specialization) {
        specializations_.put(type_arguments, specialization);
    }

    public T get(TypeVector type_arguments) {
        T t = specializations_.get(type_arguments);
        if (t != null) return t;
        return null;
    }
}
