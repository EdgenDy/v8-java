package org.jsengine.v8.internal.torque;

import org.jsengine.v8.internal.Torque;
import java.util.Vector;

public class BlockBindings<T> {
    private BindingsManager<T> manager_;
    private Vector<Binding<T>> bindings_ = new Vector<Binding<T>>();

    public BlockBindings(BindingsManager<T> manager) {
        manager_ = manager;
    }

    public void add(String name, T value, boolean mark_as_used) {
        reportErrorIfAlreadyBound(name);
        Binding binding = new Binding<T>(manager_, name, value);
        if (mark_as_used) binding.setUsed();
        bindings_.add(binding);
    }

    public void add(Identifier name, T value, boolean mark_as_used) {
        reportErrorIfAlreadyBound(name.value);
        Binding binding = new Binding<T>(manager_, name, value);
        if (mark_as_used) binding.setUsed();
        bindings_.add(binding);
    }

    public void add(String name, T value) {
        add(name, value, false);
    }

    public void add(Identifier name, T value) {
        add(name, value, false);
    }

    private void reportErrorIfAlreadyBound(String name) {
        for (Binding<T> binding : bindings_) {
            if (binding.name() == name) {
                Torque.reportError(
                        "redeclaration of name \"", name,
                        "\" in the same block is illegal, previous declaration at: ",
                        binding.declaration_position());
            }
        }
    }
}
