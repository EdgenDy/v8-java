package org.jsengine.v8.internal.torque;

public class PushBuiltinPointerInstruction extends InstructionBase {
    public static InstructionKind kKind = InstructionKind.kPushBuiltinPointerInstruction;
    public String external_name;
    public Type type;

    public PushBuiltinPointerInstruction(String external_name, Type type) {
        this.external_name = external_name;
        this.type = type;
    }

    @Override
    public void typeInstruction(Stack<Type> stack, ControlFlowGraph cfg) {
        stack.push(type);
    }
}
