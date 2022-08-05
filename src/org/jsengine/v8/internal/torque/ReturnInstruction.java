package org.jsengine.v8.internal.torque;

import java.util.Vector;

public class ReturnInstruction extends InstructionBase {
    public static InstructionKind kKind = InstructionKind.kReturnInstruction;
    public boolean IsBlockTerminator() {
        return true;
    }

    public void typeInstruction(Stack<Type> stack, ControlFlowGraph cfg) {
        cfg.setReturnType(stack.pop());
    }
}
