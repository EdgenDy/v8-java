package org.jsengine.v8.internal.torque;

public class VarDeclarationStatement extends Statement {
    public static Kind kKind = Kind.kVarDeclarationStatement;
    public boolean const_qualified;
    public Identifier name;
    public TypeExpression type;
    public Expression initializer;

    public VarDeclarationStatement(
            SourcePosition pos, boolean const_qualified, Identifier name,
            TypeExpression type,
            Expression initializer) {
        super(kKind, pos);
    }

    public VarDeclarationStatement(
            SourcePosition pos, boolean const_qualified, Identifier name,
            TypeExpression type) {
        this(pos, const_qualified, name, type, null);
    }

    public static VarDeclarationStatement dynamicCast(AstNode node) {
        if (node == null) return null;
        if (node.kind != kKind) return null;
        return (VarDeclarationStatement) node;
    }
}
