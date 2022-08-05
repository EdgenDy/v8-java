package org.jsengine.v8.internal.torque;

public class CurrentFileStreams {
    public static ThreadLocal<Scope> top_ = new ThreadLocal<Scope>();

    public static class Scope {
        public GlobalContext.PerFileStreams value_;
        public Scope previous_;

        public Scope(GlobalContext.PerFileStreams value) {
            value_ = value;
            previous_ = top();
            top(this);
        }

        public GlobalContext.PerFileStreams value() {
            return value_;
        }
    }

    public static Scope top() {
        return top_.get();
    }

    public static void top(Scope scope) {
        top_.set(scope);
    }

    public static GlobalContext.PerFileStreams get() {
        return top().value();
    }

    public static void get(GlobalContext.PerFileStreams value) {
        top().value_ = value;
    }
}
