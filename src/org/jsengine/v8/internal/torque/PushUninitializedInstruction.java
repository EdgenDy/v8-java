package org.jsengine.v8.internal.torque;

public class PushUninitializedInstruction extends InstructionBase {
    public static InstructionKind kKind = InstructionKind.kPushUninitializedInstruction;
    public Type type;

    public PushUninitializedInstruction(Type type) {
        this.type = type;
    }

    public void typeInstruction(Stack<Type> stack, ControlFlowGraph cfg) {
        stack.push(type);
    }
}
