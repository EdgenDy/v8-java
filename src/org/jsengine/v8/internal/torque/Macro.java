package org.jsengine.v8.internal.torque;

public class Macro extends Callable {
    private boolean used_;
    private Statement body_;

    protected Macro(Declarable.Kind kind, String external_name,
                    String readable_name, Signature signature,
                    Statement body) {
        super(kind, external_name, readable_name, signature, body);
        this.used_ = false;
    }

    public static Macro dynamicCast(Declarable declarable) {
        if (declarable == null) return null;
        if (!declarable.isMacro()) return null;
        return (Macro) declarable;
    }

    public Statement body() {
        return body_;
    }

    public void setUsed() {
        used_ = true;
    }
}
