package org.jsengine.v8.internal.torque;

public class ExternConstant extends Value {
    public ExternConstant(Identifier name, Type type, String value) {
        super(Declarable.Kind.kExternConstant, type, name);
        set_value(new VisitResult(type, value));
    }

    public static ExternConstant cast(Declarable declarable) {
        return (ExternConstant) declarable;
    }
}
