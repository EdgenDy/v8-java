package org.jsengine.v8.internal.torque;

public class Value extends Declarable {
    private Type type_;
    private Identifier name_;
    private VisitResult value_;

    protected Value(Kind kind, Type type, Identifier name) {
        super(kind);
        type_ = type;
        name_ = name;
    }

    public Identifier name() {
        return name_;
    }

    public VisitResult value() {
        return value_;
    }

    public Type type() {
        return type_;
    }

    public void set_value(VisitResult value) {
        value_ = value;
    }

    public static Value dynamicCast(Declarable declarable) {
        if (declarable == null) return null;
        if (!declarable.isValue()) return null;
        return (Value) declarable;
    }
}
