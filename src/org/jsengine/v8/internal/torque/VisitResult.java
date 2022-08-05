package org.jsengine.v8.internal.torque;

public class VisitResult {
    private Type type_ = null;
    private String constexpr_value_;
    private StackRange stack_range_;

    public VisitResult() {}

    public VisitResult(Type type, String constexpr_value) {
        type_ = type;
        constexpr_value_ = constexpr_value;
    }

    public VisitResult(Type type, StackRange stack_range) {
        this.type_ = type;
        this.stack_range_ = stack_range;
    }

    public Type type() {
        return type_;
    }

    public boolean isOnStack() {
        return stack_range_ != null;
    }

    public static VisitResult neverResult() {
        VisitResult result = new VisitResult();
        result.type_ = TypeOracle.getNeverType();
        return result;
    }

    public StackRange stack_range() {
        return stack_range_;
    }

    public void setType(Type new_type) {
        type_ = new_type;
    }

    public String constexpr_value() {
        return constexpr_value_;
    }
}
