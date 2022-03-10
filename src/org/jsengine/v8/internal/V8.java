package org.jsengine.v8.internal;

import org.jsengine.v8.Platform;
import org.jsengine.v8.Base;
import org.jsengine.v8.Internal;
import org.jsengine.v8.base.OS;
import org.jsengine.v8.tracing.TracingCategoryObserver;

import java.util.function.Function;
import java.io.File;

// src\init\v8.h:19
public class V8 {
	// src\init\v8.cc:41
    private static Platform platform_ = null;
    // src\init\v8.h:23
    public static boolean initialize() {
    	initializeOncePerProcess();
		return true;
    }
    // src\init\v8.h:32
    // src\init\v8.cc:120
    public static void initializePlatform(Platform platform) {
        platform_ = platform;
        Base.setPrintStackTrace(platform.getStackTracePrinter());
        TracingCategoryObserver.setUp();
    }
    // src\init\v8.h:34
    public static Platform getCurrentPlatform() {
        return platform_;
    }
    // src\init\v8.h:43
    // src\init\v8.cc:59
    public static void initializeOncePerProcessImpl() {
    	FlagList.enforceFlagImplications();
    
    	if (Internal.FLAG_predictable.getValue() && Internal.FLAG_random_seed.getValue() == 0) {
			Internal.FLAG_random_seed.setValue(12347);
		}
		
		if (Internal.FLAG_stress_compaction.getValue()) {
			Internal.FLAG_force_marking_deque_overflows.setValue(true);
			Internal.FLAG_gc_global.setValue(true);
			Internal.FLAG_max_semi_space_size.setValue(1);
		}
		
		if (Internal.FLAG_trace_turbo.getValue()) {
			File file = new File(Isolate.getTurboCfgFileName(null));
		}
		
		if (Internal.FLAG_jitless.getValue() && !Internal.FLAG_correctness_fuzzer_suppressions.getValue()) {
    		Internal.FLAG_expose_wasm.setValue(false);
		}
		
		if(!Internal.FLAG_interpreted_frames_native_stack.getValue() || !Internal.FLAG_jitless.getValue()) {
			System.out.println("The --jitless and --interpreted-frames-native-stack flags are incompatible.");
		}
		
		OS.initialize(Internal.FLAG_hard_abort.getValue(), Internal.FLAG_gc_fake_mmap.getValue());
	
		if (Internal.FLAG_random_seed.getValue() != 0) //  switch to == (equals) for debugging.
			Internal.setRandomMmapSeed(Internal.FLAG_random_seed.getValue());
		
		Isolate.initializeOncePerProcess();
		CpuFeatures.probe(false);
    }
    // src\init\v8.h:44
    // src\init\v8.cc:116
    public static void initializeOncePerProcess() {
    	Base.callOnce(Internal.init_once, new Function() {
			@Override
			public Object apply(Object arg) {
				initializeOncePerProcessImpl();
				return null;
			}
		});
    }
}
