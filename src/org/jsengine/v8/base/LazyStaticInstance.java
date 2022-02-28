package org.jsengine.v8.base;

import org.jsengine.utils.Var;
import org.jsengine.v8.Base.OnceType;

import java.util.function.Function;

public class LazyStaticInstance {
    public static class Type<T> extends LazyInstanceImpl<T> {
        public Type(CreateTrait<T> create_trait, InitOnceTrait init_once_trait, DestroyTrait<T> destroy_trait) {
            super(new StaticallyAllocatedInstanceTrait<T>(), create_trait, init_once_trait, destroy_trait);
        }
    }

    public static interface AllocationTrait<T> {
        public Var<T> mutableInstance(StorageType storage);

        public void initStorageUsingTrait(CreateTrait ConstructTrait, StorageType storage, T value);

        public static class StorageType extends Var<Object> {
            public StorageType() {
                super(null);
            }
        }
    }

    public static interface CreateTrait<T> {
        public void construct(Var<Object> allocated_ptr, T value);
    }

    public static interface InitOnceTrait {
        public <S> void init(OnceType once, Function function, S storage);
    }

    public static interface DestroyTrait<T> {
        public void destroy(T instance);
    }
}
