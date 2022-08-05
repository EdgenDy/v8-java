package org.jsengine.v8.internal.torque;

public class LabelDeclaration {
    public Identifier name;
    public TypeVector types;

    public LabelDeclaration(Identifier name, TypeVector types) {
        this.name = name;
        this.types = types;
    }
}
