package org.jsengine.v8.internal.torque;

public class PrintConstantStringInstruction extends InstructionBase {
    public static InstructionKind kKind = InstructionKind.kPrintConstantStringInstruction;
    public String message;

    public PrintConstantStringInstruction(String message) {
        this.message = message;
    }

    public void typeInstruction(Stack<Type> stack, ControlFlowGraph cfg) {

    }
}
