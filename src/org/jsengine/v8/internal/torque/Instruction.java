package org.jsengine.v8.internal.torque;

public class Instruction<T> {
    public InstructionKind kind_;
    public InstructionBase instruction_;
    public Instruction(T instr, InstructionKind kind) {
        kind_ = kind;
        instruction_ = (InstructionBase) instr;
    }

    public void typeInstruction(Stack<Type> stack, ControlFlowGraph cfg) {
        instruction_.typeInstruction(stack, cfg);
    }

    public void TypeInstruction(Stack<Type> stack, ControlFlowGraph cfg) {
        instruction_.typeInstruction(stack, cfg);
    }

    public InstructionKind kind() {
        return kind_;
    }

    public <T> T cast(Class<T> class_type) {
        return (T) instruction_;
    }
}
