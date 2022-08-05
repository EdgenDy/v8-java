package org.jsengine.v8.internal.torque;

public class AssignmentExpression extends Expression {
    public static Kind kKind = Kind.kAssignmentExpression;
    private Expression location;
    private String op;
    private Expression value;

    public  AssignmentExpression(SourcePosition pos, Expression location,
                                 Expression value) {
        this(pos, location, null, value);
    }

    public AssignmentExpression(SourcePosition pos, Expression location,
                                String op, Expression value) {
        super(kKind, pos);
        this.location = location;
        this.op = op;
        this.value = value;
    }
}

