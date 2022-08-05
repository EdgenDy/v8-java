package org.jsengine.v8.internal.torque;

import org.jsengine.v8.internal.Torque;

public class NamespaceConstantInstruction extends InstructionBase {
    public static InstructionKind kKind = InstructionKind.kNamespaceConstantInstruction;
    public NamespaceConstant constant;
    public NamespaceConstantInstruction(NamespaceConstant constant) {
        this.constant = constant;
    }

    public void typeInstruction(Stack<Type> stack, ControlFlowGraph cfg) {
        stack.pushMany(Torque.lowerType(constant.type()));
    }
}
