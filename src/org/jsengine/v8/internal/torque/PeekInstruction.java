package org.jsengine.v8.internal.torque;

import org.jsengine.v8.internal.Torque;
import org.jsengine.utils.Optional;

public class PeekInstruction extends InstructionBase {
    public static InstructionKind kKind = InstructionKind.kPeekInstruction;
    public BottomOffset slot;
    private Optional<Type> widened_type;
    public PeekInstruction(BottomOffset slot, Optional<Type> widened_type) {
        this.slot = slot;
        this.widened_type = widened_type;
    }

    public void typeInstruction(Stack<Type> stack, ControlFlowGraph cfg) {
        Type type = stack.peek(slot);
        if (widened_type.hasValue()) {
            if (type.isTopType()) {
                TopType top_type = TopType.cast(type);
                Torque.reportError("use of " + top_type.reason());
            }
            Torque.expectSubtype(type, widened_type);
            type = widened_type.get();
        }
        stack.push(type);
    }
}
