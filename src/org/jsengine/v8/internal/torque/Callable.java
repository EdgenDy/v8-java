package org.jsengine.v8.internal.torque;

public class Callable extends Scope {
    private String external_name_;
    private String readable_name_;
    private Signature signature_;
    private int returns_;
    private Statement body_;

    protected Callable(Declarable.Kind kind, String external_name,
                       String readable_name, Signature signature,
                       Statement body) {
        super(kind);
        external_name_ = external_name;
        readable_name_ = readable_name;
        signature_ = signature;
        returns_ = 0;
        body_ = body;
    }

    public Signature signature() {
        return signature_;
    }

    public static Callable dynamicCast(Declarable declarable) {
        if (declarable == null) return null;
        if (!declarable.isCallable()) return null;
        return (Callable) declarable;
    }
    public boolean shouldBeInlined() {
        return false;
    }
    public boolean shouldGenerateExternalCode() {
        return !shouldBeInlined();
    }

    public String externalName() {
        return external_name_;
    }

    public NameVector parameter_names() {
        return signature_.parameter_names;
    }

    public boolean isExternal() {
        return !(body_ != null);
    }

    public static Callable cast(Declarable declarable) {
        return (Callable) declarable;
    }

    public String readableName() {
        return readable_name_;
    }

    public boolean isTransitioning() {
        return signature().transitioning;
    }
}
