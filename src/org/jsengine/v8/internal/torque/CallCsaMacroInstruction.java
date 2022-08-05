package org.jsengine.v8.internal.torque;

import org.jsengine.v8.internal.Torque;
import java.util.Vector;

public class CallCsaMacroInstruction extends InstructionBase {
    public static InstructionKind kKind = InstructionKind.kCallCsaMacroInstruction;
    public Macro macro;
    Vector<String> constexpr_arguments;
    Block catch_block;

    public CallCsaMacroInstruction(Macro macro, Vector<String> constexpr_arguments, Block catch_block) {
        this.macro = macro;
        this.constexpr_arguments = constexpr_arguments;
        this.catch_block = catch_block;
    }

    public void typeInstruction(Stack<Type> stack, ControlFlowGraph cfg) {
        TypeVector parameter_types = Torque.lowerParameterTypes(macro.signature().parameter_types);
        for (int i = parameter_types.size() - 1; i >= 0; --i) {
            Type arg_type = stack.pop();
            Type parameter_type = parameter_types.lastElement();
            parameter_types.removeElementAt(parameter_types.size() - 1);
            if (arg_type != parameter_type) {
                Torque.reportError("parameter ", i, ": expected type ", parameter_type,
                        " but found type ", arg_type);
            }
        }

        if (macro.isTransitioning()) {
            invalidateTransientTypes(stack);
        }

        if (catch_block != null) {
            Stack<Type> catch_stack = stack;
            catch_stack.push(TypeOracle.getJSAnyType());
            catch_block.setInputTypes(catch_stack);
        }

        stack.pushMany(Torque.lowerType(macro.signature().return_type));
    }
}
