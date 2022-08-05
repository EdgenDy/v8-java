package org.jsengine.v8.internal.torque;

public class BottomOffset {
    public int offset;

    public BottomOffset(int offset) {
        this.offset = offset;
    }

    public BottomOffset add(int x) {
        return new BottomOffset(offset + x);
    }

    public BottomOffset subtract(int x) {
        return new BottomOffset(offset - x);
    }

    public boolean lessThan(BottomOffset other) {
        return offset < other.offset;
    }

    public boolean greaterThan(BottomOffset other) {
        return offset > other.offset;
    }

    public BottomOffset increment() {
        ++offset;
        return this;
    }
}
