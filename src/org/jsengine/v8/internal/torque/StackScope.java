package org.jsengine.v8.internal.torque;

public class StackScope {
    private ImplementationVisitor visitor_;
    private BottomOffset base_;
    private boolean closed_ = false;

    public StackScope(ImplementationVisitor visitor) {
        visitor_ = visitor;
        base_ = visitor_.assembler().currentStack().aboveTop();
    }

    public VisitResult yield(VisitResult result) {
        closed_ = true;
        if (!result.isOnStack()) {
            if (!visitor_.assembler().currentBlockIsComplete()) {
                visitor_.assembler().dropTo(base_);
            }
            return result;
        }

        visitor_.assembler().dropTo(result.stack_range().end());
        visitor_.assembler().deleteRange(
                new StackRange(base_, result.stack_range().begin()));
        base_ = visitor_.assembler().currentStack().aboveTop();
        return VisitResult(result.type(), visitor_.assembler().topRange(
                result.stack_range().Size()));
    }
}
