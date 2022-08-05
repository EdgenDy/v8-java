package org.jsengine.v8.internal.torque;

public class RuntimeFunction extends Callable {
    public RuntimeFunction(String name, Signature signature) {
        super(Declarable.Kind.kRuntimeFunction, name, name, signature, null);
    }

    public static RuntimeFunction dynamicCast(Declarable declarable) {
        if (declarable == null) return null;
        if (!declarable.isRuntimeFunction()) return null;
        return (RuntimeFunction) declarable;
    }
}
