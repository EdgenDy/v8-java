package org.jsengine.v8.internal.torque;

import org.jsengine.v8.internal.Torque;

import java.util.Vector;

public class CallCsaMacroAndBranchInstruction extends InstructionBase {
    public static InstructionKind kKind = InstructionKind.kCallCsaMacroAndBranchInstruction;
    public Macro macro;
    public Vector<String> constexpr_arguments;
    public Block return_continuation;
    public Vector<Block> label_blocks;
    public Block catch_block;

    public CallCsaMacroAndBranchInstruction(Macro macro, Vector<String> constexpr_arguments,
                                            Block return_continuation, Vector<Block> label_blocks,
                                            Block catch_block) {
        this.macro = macro;
        this.constexpr_arguments = constexpr_arguments;
        this.return_continuation = return_continuation;
        this.label_blocks = label_blocks;
        this.catch_block = catch_block;
    }

    public void typeInstruction(Stack<Type> stack, ControlFlowGraph cfg) {
        Vector<Type> parameter_types = Torque.lowerParameterTypes(macro.signature().parameter_types);
        for (int i = parameter_types.size() - 1; i >= 0; --i) {
            Type arg_type = stack.pop();
            Type parameter_type = parameter_types.lastElement();
            parameter_types.removeElementAt(parameter_types.size() - 1);
            if (arg_type != parameter_type) {
                Torque.reportError("parameter ", i, ": expected type ", parameter_type,
                        " but found type ", arg_type);
            }
        }

        if (label_blocks.size() != macro.signature().labels.size()) {
            Torque.reportError("wrong number of labels");
        }
        for (int i = 0; i < label_blocks.size(); ++i) {
            Stack<Type> continuation_stack = stack;
            continuation_stack.pushMany(Torque.lowerParameterTypes(macro.signature().labels.get(i).types));
            label_blocks.get(i).setInputTypes(continuation_stack);
        }

        if (macro.isTransitioning()) {
            invalidateTransientTypes(stack);
        }

        if (catch_block != null) {
            Stack<Type> catch_stack = stack;
            catch_stack.push(TypeOracle.getJSAnyType());
            catch_block.setInputTypes(catch_stack);
        }

        if (macro.signature().return_type != TypeOracle.getNeverType()) {
            Stack<Type> return_stack = stack;
            return_stack.pushMany(Torque.lowerType(macro.signature().return_type));
            if (return_continuation == null) {
                Torque.reportError("missing return continuation.");
            }
            return_continuation.setInputTypes(return_stack);
        } else {
            if (return_continuation != null) {
                Torque.reportError("unreachable return continuation.");
            }
        }
    }
}
