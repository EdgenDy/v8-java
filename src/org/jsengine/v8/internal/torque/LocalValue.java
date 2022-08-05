package org.jsengine.v8.internal.torque;

public class LocalValue {
    public boolean is_const;
    public VisitResult value;

    public LocalValue(boolean is_const, VisitResult value) {
        this.is_const = is_const;
        this.value = value;
    }
}
