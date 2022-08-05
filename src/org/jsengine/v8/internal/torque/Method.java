package org.jsengine.v8.internal.torque;

public class Method extends TorqueMacro {
    private AggregateType aggregate_type_;

    public Method(AggregateType aggregate_type, String external_name,
                  String readable_name, Signature signature, Statement body) {
        super(Declarable.Kind.kMethod, external_name,
                readable_name, signature, body, true, false);
        aggregate_type_ = aggregate_type;
    }

    public AggregateType aggregate_type() {
        return aggregate_type_;
    }

    public static Method cast(Declarable declarable) {
        return (Method) declarable;
    }

    public static Method dynamicCast(Declarable declarable) {
        if (declarable == null) return null;
        if (!declarable.isMethod()) return null;
        return (Method) declarable;
    }
}
