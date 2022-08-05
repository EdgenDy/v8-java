package org.jsengine.v8.internal.torque;

public class TorqueMacro extends Macro {
    private boolean exported_to_csa_ = false;
    protected TorqueMacro(Declarable.Kind kind, String external_name,
                          String readable_name, Signature signature,
                          Statement body, boolean is_user_defined,
                          boolean exported_to_csa) {
        super(kind, external_name, readable_name, signature, body);
        this.exported_to_csa_ = exported_to_csa;
    }

    public TorqueMacro(String external_name, String readable_name,
              Signature signature, Statement body,
                       boolean is_user_defined, boolean exported_to_csa) {
        this(Declarable.Kind.kTorqueMacro, external_name, readable_name,
                signature, body, is_user_defined, exported_to_csa);
    }

    public static TorqueMacro cast(Declarable declarable) {
        return (TorqueMacro) declarable;
    }
}
