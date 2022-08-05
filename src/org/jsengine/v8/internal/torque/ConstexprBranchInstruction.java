package org.jsengine.v8.internal.torque;

import java.util.Vector;

public class ConstexprBranchInstruction extends InstructionBase {
    public static InstructionKind kKind = InstructionKind.kConstexprBranchInstruction;
    public String condition;
    public Block if_true;
    public Block if_false;
    public ConstexprBranchInstruction(String condition, Block if_true, Block if_false) {
        this.condition = condition;
        this.if_true = if_true;
        this.if_false = if_false;
    }

    public boolean isBlockTerminator() {
        return true;
    }

    public void appendSuccessorBlocks(Vector<Block> block_list) {
        block_list.add(if_true);
        block_list.add(if_false);
    }

    public void typeInstruction(Stack<Type> stack, ControlFlowGraph cfg) {
        if_true.setInputTypes(stack);
        if_false.setInputTypes(stack);
    }
}
