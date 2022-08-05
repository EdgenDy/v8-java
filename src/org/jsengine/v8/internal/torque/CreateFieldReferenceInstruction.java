package org.jsengine.v8.internal.torque;

import org.jsengine.v8.internal.Torque;

public class CreateFieldReferenceInstruction extends InstructionBase {
    public static InstructionKind kKind = InstructionKind.kCreateFieldReferenceInstruction;
    public Type type;
    public String field_name;

    public CreateFieldReferenceInstruction(Type type, String field_name) {
        this.type = type;
        this.field_name = field_name;
    }

    public void typeInstruction(Stack<Type> stack, ControlFlowGraph cfg) {
        Torque.expectSubtype(stack.top(), type);
        stack.push(TypeOracle.getIntPtrType());
    }
}
