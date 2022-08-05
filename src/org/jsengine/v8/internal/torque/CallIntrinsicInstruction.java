package org.jsengine.v8.internal.torque;

import org.jsengine.v8.internal.Torque;
import java.util.Vector;

public class CallIntrinsicInstruction extends InstructionBase {
    public Intrinsic intrinsic;
    public TypeVector specialization_types;
    public Vector<String> constexpr_arguments;
    public CallIntrinsicInstruction(Intrinsic intrinsic,
                                    TypeVector specialization_types,
                                    Vector<String> constexpr_arguments) {
        this.intrinsic = intrinsic;
        this.specialization_types = specialization_types;
        this.constexpr_arguments = constexpr_arguments;
    }

    public void typeInstruction(Stack<Type> stack, ControlFlowGraph cfg) {
        Vector<Type> parameter_types = Torque.lowerParameterTypes(intrinsic.signature().parameter_types);
        for (int i = parameter_types.size() - 1; i >= 0; --i) {
            Type arg_type = stack.pop();
            Type parameter_type = parameter_types.lastElement();
            parameter_types.removeElementAt(parameter_types.size() - 1);
            if (arg_type != parameter_type) {
                Torque.reportError("parameter ", i, ": expected type ", parameter_type,
                        " but found type ", arg_type);
            }
        }
        if (intrinsic.isTransitioning()) {
            invalidateTransientTypes(stack);
        }
        stack.pushMany(Torque.lowerType(intrinsic.signature().return_type));
    }
}
