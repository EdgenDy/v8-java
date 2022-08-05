package org.jsengine.v8.internal.torque;

import org.jsengine.v8.internal.Torque;

public abstract class InstructionBase {
    public SourcePosition pos;
    public InstructionBase() {
        pos = CurrentSourcePosition.get();
    }

    public abstract void typeInstruction(Stack<Type> stack, ControlFlowGraph cfg);

    public boolean isBlockTerminator() {
        return false;
    }

    public void invalidateTransientTypes(Stack<Type> stack) {
        Stack.Iterator<Type> current = stack.begin();
        while (current.notEquals(stack.end())) {
            if (current.value().isTransient()) {
                StringBuilder stream = new StringBuilder();
                stream.append("type ").append(current.value())
                        .append(" is made invalid by transitioning callable invocation at ")
                        .append(Torque.positionAsString(pos));
                current.value(TypeOracle.getTopType(stream.toString(), current.value()));
            }
            current.increase(1);
        }
    }
}
