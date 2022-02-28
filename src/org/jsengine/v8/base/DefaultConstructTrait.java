package org.jsengine.v8.base;

import org.jsengine.utils.Var;

public class DefaultConstructTrait<T> implements LazyStaticInstance.CreateTrait<T> {
    public void construct(Var<Object> allocated_ptr, T value) {
        allocated_ptr.setValue((Object) value);
    }
}
