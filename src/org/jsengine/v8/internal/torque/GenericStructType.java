package org.jsengine.v8.internal.torque;

import java.util.Vector;

public class GenericStructType extends Declarable {
    private String name_;
    private StructDeclaration declaration_;
    private SpecializationMap<StructType> specializations_ = new SpecializationMap<StructType>();

    public GenericStructType(String name, StructDeclaration declaration) {
        super(Kind.kGenericStructType);
        this.name_ = name;
        this.declaration_ = declaration;
    }

    static GenericStructType dynamicCast(Declarable declarable) {
        if (declarable == null) return null;
        if (!declarable.isGenericStructType()) return null;
        return (GenericStructType) declarable;
    }

    public Vector<Identifier> generic_parameters() {
        return declaration_.generic_parameters;
    }

    public SpecializationMap<StructType> specializations() {
        return specializations_;
    }

    public StructDeclaration declaration() {
        return declaration_;
    }
}
