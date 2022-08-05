package org.jsengine.v8.internal.torque;

import org.jsengine.v8.internal.Torque;

public class Intrinsic extends Callable {
    public Intrinsic(String name, Signature signature) {
        super(Declarable.Kind.kIntrinsic, name, name, signature, null);
        if (signature.parameter_types.var_args) {
            Torque.reportError("Varargs are not supported for intrinsics.");
        }
    }

    public static Intrinsic dynamicCast(Declarable declarable) {
        if (declarable == null) return null;
        if (!declarable.isIntrinsic()) return null;
        return (Intrinsic) declarable;
    }
}
