package org.jsengine.v8.internal.torque;

import org.jsengine.v8.internal.Torque;

import java.util.Vector;

public class GotoExternalInstruction extends InstructionBase {
    public static InstructionKind kKind = InstructionKind.kGotoExternalInstruction;
    public String destination;
    public Vector<String> variable_names;
    public GotoExternalInstruction(String destination, Vector<String> variable_names) {
        this.destination = destination;
        this.variable_names = variable_names;
    }

    public void typeInstruction(Stack<Type> stack, ControlFlowGraph cfg) {
        if (variable_names.size() != stack.size()) {
            Torque.reportError("goto external label with wrong parameter count.");
        }
    }
}
