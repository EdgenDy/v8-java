package org.jsengine.v8.internal.torque;

public class AbortInstruction extends InstructionBase {
    public static InstructionKind kKind = InstructionKind.kAbortInstruction;
    public enum Kind {
        kDebugBreak,
        kUnreachable,
        kAssertionFailure
    };

    public Kind kind;
    public String message;

    public boolean isBlockTerminator() {
        return kind != Kind.kDebugBreak;
    }

    public AbortInstruction(Kind kind, String message) {

    }

    public AbortInstruction(Kind kind) {
        this(kind, "");
    }

    public void typeInstruction(Stack<Type> stack, ControlFlowGraph cfg) {

    }
}
