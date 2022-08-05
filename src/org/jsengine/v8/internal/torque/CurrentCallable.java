package org.jsengine.v8.internal.torque;

public class CurrentCallable {
    public static ThreadLocal<Scope> top_ = new ThreadLocal<Scope>();

    public static class Scope {
        public Callable value_;
        public Scope previous_;

        public Scope(Callable value) {
            value_ = value;
            previous_ = top();
            top(this);
        }

        public Callable value() {
            return value_;
        }
    }

    public static Scope top() {
        return top_.get();
    }

    public static void top(Scope scope) {
        top_.set(scope);
    }

    public static Callable get() {
        return top().value();
    }
}
