package org.jsengine.v8.internal.torque;

import org.jsengine.v8.internal.Torque;

import java.util.Vector;

public class CallBuiltinPointerInstruction extends InstructionBase {
    public static InstructionKind kKind = InstructionKind.kCallBuiltinPointerInstruction;
    public boolean is_tailcall;
    public BuiltinPointerType type;
    public int argc;

    public CallBuiltinPointerInstruction(boolean is_tailcall, BuiltinPointerType type, int argc) {
        this.is_tailcall = is_tailcall;
        this.type = type;
        this.argc = argc;
    }

    public boolean IsBlockTerminator() {
        return is_tailcall;
    }

    public void typeInstruction(Stack<Type> stack, ControlFlowGraph cfg) {
        Vector<Type> argument_types = stack.popMany(argc);
        BuiltinPointerType f = BuiltinPointerType.dynamicCast(stack.pop());
        if (f == null) Torque.reportError("expected function pointer type");
        if (argument_types != Torque.lowerParameterTypes(f.parameter_types())) {
            Torque.reportError("wrong argument types");
        }
        invalidateTransientTypes(stack);
        stack.pushMany(Torque.lowerType(f.return_type()));
    }
}
