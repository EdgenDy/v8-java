package org.jsengine.v8.internal.torque;

public class ReturnStatement extends Statement {
    public static Kind kKind = Kind.kReturnStatement;

    public ReturnStatement(SourcePosition pos, Expression value) {
        super(kKind, pos);
    }
}
