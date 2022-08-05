package org.jsengine.v8.internal.torque;

import org.jsengine.v8.internal.Torque;

import java.util.Vector;

public class CallRuntimeInstruction extends InstructionBase {
    public boolean is_tailcall;
    public RuntimeFunction runtime_function;
    public int argc;
    public Block catch_block;

    public CallRuntimeInstruction(boolean is_tailcall, RuntimeFunction runtime_function,
                                  int argc, Block catch_block) {
        this.is_tailcall = is_tailcall;
        this.runtime_function = runtime_function;
        this.argc = argc;
        this.catch_block = catch_block;
    }

    public void typeInstruction(Stack<Type> stack, ControlFlowGraph cfg) {
        Vector<Type> argument_types = stack.popMany(argc);
        if (argument_types !=
                Torque.lowerParameterTypes(runtime_function.signature().parameter_types,
                        argc)) {
            Torque.reportError("wrong argument types");
        }
        if (runtime_function.isTransitioning()) {
            invalidateTransientTypes(stack);
        }

        if (catch_block != null) {
            Stack<Type> catch_stack = stack;
            catch_stack.push(TypeOracle.getJSAnyType());
            catch_block.setInputTypes(catch_stack);
        }

        Type return_type = runtime_function.signature().return_type;
        if (return_type != TypeOracle.getNeverType()) {
            stack.pushMany(Torque.lowerType(return_type));
        }
    }
}
