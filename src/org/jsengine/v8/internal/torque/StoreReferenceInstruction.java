package org.jsengine.v8.internal.torque;

import org.jsengine.v8.internal.Torque;

import java.util.Vector;

public class StoreReferenceInstruction extends InstructionBase {
    public static InstructionKind kKind = InstructionKind.kStoreReferenceInstruction;
    public Type type;

    public StoreReferenceInstruction(Type type) {
        this.type = type;
    }

    public void typeInstruction(Stack<Type> stack, ControlFlowGraph cfg) {
        Torque.expectSubtype(stack.pop(), type);
        Torque.expectType(TypeOracle.getIntPtrType(), stack.pop());
        Torque.expectSubtype(stack.pop(), TypeOracle.getHeapObjectType());
    }
}
