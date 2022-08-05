package org.jsengine.v8.internal.torque;

import java.util.Vector;

public class BlockStatement extends Statement {
    public static Kind kKind = Kind.kBlockStatement;
    public boolean deferred;
    public Vector<Statement> statements;

    public BlockStatement(SourcePosition pos, boolean deferred,
                          Vector<Statement> statements) {
        super(kKind, pos);
        this.deferred = deferred;
        this.statements = statements;
    }

    public BlockStatement(SourcePosition pos, boolean deferred) {
        this(pos, deferred, new Vector<Statement>());
    }

    public BlockStatement(SourcePosition pos) {
        this(pos, false, new Vector<Statement>());
    }
}
