package org.jsengine.v8.internal.torque;

import org.jsengine.v8.internal.Torque;
import java.util.Vector;

public class Block {
    private ControlFlowGraph cfg_;
    private Stack<Type> input_types_;
    private int id_;
    private boolean is_deferred_;
    private Vector<Instruction> instructions_ = new Vector<Instruction>();

    public Block(ControlFlowGraph cfg, int id,
                    Stack<Type> input_types, boolean is_deferred) {
        cfg_ = cfg;
        input_types_ = input_types;
        id_ = id;
        is_deferred_ = is_deferred;
    }

    public void add(Instruction instruction) {
        instructions_.add(instruction);
    }

    public boolean isComplete() {
        return !instructions_.isEmpty() && instructions_.lastElement().instruction_.isBlockTerminator();
    }

    public Stack<Type> inputTypes() {
        return input_types_;
    }

    public boolean hasInputTypes() {
        return input_types_ != null;
    }

    public void setInputTypes(Stack<Type> input_types) {
        if (input_types_ == null) {
            input_types_ = input_types;
            return;
        } else if (input_types_ == input_types) {
            return;
        }

        Stack<Type> merged_types = new Stack<Type>();
        boolean widened = false;
        Stack.Iterator<Type> c2_iterator = input_types.begin();
        for (Type c1 : input_types_) {
            Type merged_type = TypeOracle.getUnionType(c1, c2_iterator.value());
            c2_iterator.increase(1);
            if (!merged_type.isSubtypeOf(c1)) {
                widened = true;
            }
            merged_types.push(merged_type);
        }
        if (merged_types.size() == input_types_.size()) {
            if (widened) {
                input_types_ = merged_types;
                retype();
            }
            return;
        }

        StringBuilder error = new StringBuilder();
        error.append("incompatible types at branch:\n");
        for (int i = Math.max(input_types_.size(), input_types.size()) - 1; i >= 0; --i) {
            Type left = null;
            Type right = null;
            if (i < input_types.size()) {
                left = input_types.peek(new BottomOffset(i));
            }
            if (i < input_types_.size()) {
                right = input_types_.peek(new BottomOffset(i));
            }
            if (left != null && right != null && left == right) {
                error.append(left).append("\n");
            } else {
                if (left != null) {
                    error.append(left);
                } else {
                    error.append("/*missing*/");
                }
                error.append("   =>   ");
                if (right != null) {
                    error.append(right);
                } else {
                    error.append("/*missing*/");
                }
                error.append("\n");
            }
        }
        Torque.reportError(error.toString());
    }

    public void retype() {
        Stack<Type> current_stack = inputTypes();
        for (Instruction instruction : instructions()) {
            instruction.TypeInstruction(current_stack, cfg_);
        }
    }

    public Vector<Instruction> instructions() {
        return instructions_;
    }

    public boolean isDeferred() {
        return is_deferred_;
    }

    public int id() {
        return id_;
    }
}
