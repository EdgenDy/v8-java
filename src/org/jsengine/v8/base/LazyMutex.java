package org.jsengine.v8.base;

public class LazyMutex extends LazyStaticInstance.Type<Mutex> {
    public LazyMutex() {
        super(new DefaultConstructTrait<Mutex>(), new ThreadSafeInitOnceTrait(), new LeakyInstanceTrait<Mutex>());
    }
}
