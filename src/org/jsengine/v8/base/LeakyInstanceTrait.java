package org.jsengine.v8.base;

public class LeakyInstanceTrait<T> implements  LazyStaticInstance.DestroyTrait<T> {
    public void destroy(T instance) {

    }
}
