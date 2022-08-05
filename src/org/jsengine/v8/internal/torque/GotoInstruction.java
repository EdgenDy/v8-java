package org.jsengine.v8.internal.torque;

public class GotoInstruction extends InstructionBase {
    public static InstructionKind kKind = InstructionKind.kGotoInstruction;
    public Block destination;

    public GotoInstruction(Block destination) {
        this.destination = destination;
    }

    public void typeInstruction(Stack<Type> stack, ControlFlowGraph cfg) {
        destination.setInputTypes(stack);
    }
}
