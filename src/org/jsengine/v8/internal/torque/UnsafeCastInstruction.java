package org.jsengine.v8.internal.torque;

public class UnsafeCastInstruction extends InstructionBase {
    public static InstructionKind kKind = InstructionKind.kUnsafeCastInstruction;
    public Type destination_type;

    public UnsafeCastInstruction(Type destination_type) {
        this.destination_type = destination_type;
    }

    public void typeInstruction(Stack<Type> stack, ControlFlowGraph cfg) {
        stack.poke(stack.aboveTop().subtract(1), destination_type);
    }
}
