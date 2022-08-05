package org.jsengine.v8.internal.torque;

public class SpecializationKey<T> {
    public T generic;
    public TypeVector specialized_types;

    public SpecializationKey(T generic, TypeVector specialized_types) {
        this.generic = generic;
        this.specialized_types = specialized_types;
    }

    public SpecializationKey() {
        generic = null;
        specialized_types = new TypeVector();
    }
}
