package org.jsengine.v8.internal.torque;

public class NamespaceConstant extends Value {
    private String external_name_;
    private Expression body_;

    public NamespaceConstant(Identifier constant_name, String external_name,
                             Type type, Expression body) {
        super(Declarable.Kind.kNamespaceConstant, type, constant_name);
        external_name_ = external_name;
        body_ = body;
    }

    public String external_name() {
        return external_name_;
    }

    public static NamespaceConstant cast(Declarable declarable) {
        return (NamespaceConstant) declarable;
    }

    public static NamespaceConstant dynamicCast(Declarable declarable) {
        if (declarable == null) return null;
        if (!declarable.isNamespaceConstant()) return null;
        return (NamespaceConstant) declarable;
    }
}
