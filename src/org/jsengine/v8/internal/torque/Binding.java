package org.jsengine.v8.internal.torque;

public class Binding<T> {
    public T super_class;
    private String name_;
    private BindingsManager<T> manager_;
    private Binding previous_binding_;
    private boolean used_;
    private boolean written_;
    private SourcePosition declaration_position_ = CurrentSourcePosition.get();

    public Binding(BindingsManager<T> manager, String name, T t) {
        super_class = t;
        manager_ = manager;
        name_ = name;
        previous_binding_ = this;
        used_ = false;
        written_ = false;
    }

    public Binding(BindingsManager<T> manager, Identifier name, T t) {
        this(manager, name.value, t);
        declaration_position_ = name.pos;
    }

    public String name() {
        return name_;
    }

    public void setUsed() {
        used_ = true;
    }

    public SourcePosition declaration_position() {
        return declaration_position_;
    }
}
