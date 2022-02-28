package org.jsengine.v8.base;

import org.jsengine.v8.Base.OnceType;
import org.jsengine.v8.Base;

import java.util.function.Function;

public class ThreadSafeInitOnceTrait implements LazyStaticInstance.InitOnceTrait {
    public <S> void init(OnceType once, Function function, S storage) {
        Base.callOnce(once, function, storage);
    }
}
