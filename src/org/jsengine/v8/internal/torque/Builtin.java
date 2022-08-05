package org.jsengine.v8.internal.torque;

public class Builtin extends Callable {
    public enum Kind {
        kStub,
        kFixedArgsJavaScript,
        kVarArgsJavaScript
    };

    public Kind kind_;
    public Builtin(String external_name, String readable_name,
                   Builtin.Kind kind, Signature signature,
                   Statement body) {
        super(Declarable.Kind.kBuiltin, external_name, readable_name, signature, body);
        kind_ = kind;
    }

    public static Builtin cast(Declarable declarable) {
        return (Builtin) declarable;
    }

    public static Builtin dynamicCast(Declarable declarable) {
        if (declarable == null) return null;
        if (!declarable.isBuiltin()) return null;
        return (Builtin) declarable;
    }
}
