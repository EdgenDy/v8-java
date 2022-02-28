package org.jsengine.v8.base;

import org.jsengine.utils.Var;

public class StaticallyAllocatedInstanceTrait<T> implements LazyStaticInstance.AllocationTrait<T> {
    public void initStorageUsingTrait(LazyStaticInstance.CreateTrait constructTrait, StorageType storage, T value) {
        constructTrait.construct(storage, value);
    }

    public Var<T> mutableInstance(StorageType storage) {
        return new Var<T>(storage);
    }
}
