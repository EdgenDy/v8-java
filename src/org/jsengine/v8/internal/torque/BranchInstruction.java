package org.jsengine.v8.internal.torque;

import org.jsengine.v8.internal.Torque;
import java.util.Vector;

public class BranchInstruction extends InstructionBase {
    public static InstructionKind kKind = InstructionKind.kBranchInstruction;
    public Block if_true;
    public Block if_false;

    public BranchInstruction(Block if_true, Block if_false) {
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
        Type condition_type = stack.pop();
        if (condition_type != TypeOracle.getBoolType()) {
            Torque.reportError("condition has to have type bool");
        }
        if_true.setInputTypes(stack);
        if_false.setInputTypes(stack);
    }
}
