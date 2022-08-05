package org.jsengine.v8.internal.torque;

public class ParameterTypes {
    public TypeVector types;
    public boolean var_args;

    public ParameterTypes(TypeVector types, boolean var_args) {
        this.types = types;
        this.var_args = var_args;
    }
}
