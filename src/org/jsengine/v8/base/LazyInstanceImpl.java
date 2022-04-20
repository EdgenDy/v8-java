package org.jsengine.v8.base;

import org.jsengine.utils.Var;
import org.jsengine.v8.base.Mutex;
import org.jsengine.v8.Base.OnceType;

import java.util.function.Function;

public class LazyInstanceImpl<T> {
    private LazyStaticInstance.AllocationTrait AllocationTrait;
    private LazyStaticInstance.CreateTrait CreateTrait;
    private LazyStaticInstance.InitOnceTrait InitOnceTrait;
    private LazyStaticInstance.DestroyTrait DestroyTrait;

    public OnceType once_ = new OnceType();

    public LazyStaticInstance.AllocationTrait.StorageType storage_ =
            new LazyStaticInstance.AllocationTrait.StorageType();

    public LazyInstanceImpl(LazyStaticInstance.AllocationTrait AllocationTrait, LazyStaticInstance.CreateTrait CreateTrait, LazyStaticInstance.InitOnceTrait InitOnceTrait, LazyStaticInstance.DestroyTrait DestroyTrait) {
        this.AllocationTrait = AllocationTrait;
        this.CreateTrait = CreateTrait;
        this.InitOnceTrait = InitOnceTrait;
        this.DestroyTrait = DestroyTrait;
    }

    public LazyInstanceImpl() {}

    private void initInstance(Object storage) {
        this.AllocationTrait.initStorageUsingTrait(CreateTrait, (LazyStaticInstance.AllocationTrait.StorageType) storage, new Mutex());
    }

    private void init() {
        InitOnceTrait.init(once_, new Function<Object, Void>() {
            public Void apply(Object args) {
                initInstance(args);
                return null;
            }
        }, storage_);
    }

    public Var<T> pointer() {
        init();
        return AllocationTrait.mutableInstance(storage_);
    }
}
