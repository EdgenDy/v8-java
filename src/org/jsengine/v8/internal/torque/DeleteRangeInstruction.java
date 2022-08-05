package org.jsengine.v8.internal.torque;

public class DeleteRangeInstruction extends InstructionBase {
    public static InstructionKind kKind = InstructionKind.kDeleteRangeInstruction;
    public StackRange range;
    public DeleteRangeInstruction(StackRange range) {
        this.range = range;
    }

    public void typeInstruction(Stack<Type> stack, ControlFlowGraph cfg) {
        stack.deleteRange(range);
    }
}
