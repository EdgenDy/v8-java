package org.jsengine.v8.internal.torque;

public class StackRange {
    private BottomOffset begin_;
    private BottomOffset end_;

    public StackRange(BottomOffset begin, BottomOffset end) {
        this.begin_ = begin;
        this.end_ = end;
    }

    public void extend(StackRange adjacent) {
        end_ = adjacent.end_;
    }

    public int size() {
        return end_.offset - begin_.offset;
    }

    public BottomOffset begin() {
        return begin_;
    }

    public BottomOffset end() {
        return end_;
    }
}
