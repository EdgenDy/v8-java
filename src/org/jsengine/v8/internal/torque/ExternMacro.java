package org.jsengine.v8.internal.torque;

public class ExternMacro extends Macro {
    private String external_assembler_name_;
    public ExternMacro(String name, String external_assembler_name, Signature signature) {
        super(Declarable.Kind.kExternMacro, name, name, signature, null);
        external_assembler_name_ = external_assembler_name;
    }

    public static ExternMacro cast(Declarable declarable) {
        return (ExternMacro) declarable;
    }

    public static ExternMacro dynamicCast(Declarable declarable) {
        if (declarable == null) return null;
        if (!declarable.isExternMacro()) return null;
        return (ExternMacro) declarable;
    }

    public String external_assembler_name() {
        return external_assembler_name_;
    }
}
