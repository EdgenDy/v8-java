package org.jsengine.v8.internal.torque;

import org.jsengine.v8.internal.Torque;

public class LoadReferenceInstruction extends InstructionBase {
    public Type type;
    public static InstructionKind kKind;
    public LoadReferenceInstruction(Type type) {
        this.type = type;
    }

    public void typeInstruction(Stack<Type> stack, ControlFlowGraph cfg) {
        Torque.expectType(TypeOracle.getIntPtrType(), stack.pop());
        Torque.expectSubtype(stack.pop(), TypeOracle.getHeapObjectType());
        stack.push(type);
    }
}
