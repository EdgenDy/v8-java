package org.jsengine.v8.internal.torque;

import org.jsengine.v8.internal.Torque;
import java.util.Vector;

public class CallBuiltinInstruction extends InstructionBase {
    public static InstructionKind kKind = InstructionKind.kCallBuiltinInstruction;
    public boolean is_tailcall;
    public Builtin builtin;
    public int argc = 0;
    Block catch_block = null;

    public CallBuiltinInstruction(boolean is_tailcall, Builtin builtin, int argc, Block catch_block) {
        this.is_tailcall = is_tailcall;
        this.builtin = builtin;
        this.argc = argc;
        this.catch_block = catch_block;
    }

    @Override
    public boolean isBlockTerminator() {
        return is_tailcall;
    }

    public void typeInstruction(Stack<Type> stack, ControlFlowGraph cfg) {
        Vector<Type> argument_types = stack.popMany(argc);
        if (argument_types != Torque.lowerParameterTypes(builtin.signature().parameter_types)) {
            Torque.reportError("wrong argument types");
        }
        if (builtin.isTransitioning()) {
            InvalidateTransientTypes(stack);
        }

        if (catch_block != null) {
            Stack<Type> catch_stack = stack;
            catch_stack.push(TypeOracle.getJSAnyType());
            catch_block.setInputTypes(catch_stack);
        }

        stack.pushMany(Torque.lowerType(builtin.signature().return_type));
    }
}
