package org.jsengine.v8.internal.torque;

import org.jsengine.utils.UnorderedMap;
import org.jsengine.v8.internal.Torque;

public class BindingsManager<T> {
    private UnorderedMap<String, Binding<T>> current_bindings_ = new UnorderedMap<String, Binding<T>>();

    public Binding<T> tryLookup(String name) {
        if (name.length() >= 2 && name.charAt(0) == '_' && name.charAt(1) != '_') {
            Torque.error("Trying to reference '", name, "' which is marked as unused.").Throw();
        }
        Binding<T> binding = current_bindings_.get(name);

        if (binding != null) {
            binding.setUsed();
        }
        return binding;
    }
}
