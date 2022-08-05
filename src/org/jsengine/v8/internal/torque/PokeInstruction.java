package org.jsengine.v8.internal.torque;

import org.jsengine.v8.internal.Torque;

public class PokeInstruction extends InstructionBase {
    public static InstructionKind kKind = InstructionKind.kPokeInstruction;
    public BottomOffset slot;
    public Type widened_type;
    public PokeInstruction(BottomOffset slot, Type widened_type) {
        this.slot = slot;
        this.widened_type = widened_type;
    }

    public void typeInstruction(Stack<Type> stack, ControlFlowGraph cfg) {
        Type type = stack.top();
        if (widened_type != null) {
            Torque.expectSubtype(type, widened_type);
            type = widened_type;
        }
        stack.poke(slot, type);
        stack.pop();
    }
}
