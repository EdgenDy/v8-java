package org.jsengine.v8;

import org.jsengine.v8.platform.DefaultPlatform;

// include\v8-platform.h:283
public abstract class Platform {
    // include\v8-platform.h:434
    public abstract TracingController getTracingController();

    public static Platform newDefaultPlatform(int thread_pool_size, IdleTaskSupport idle_task_support, InProcessStackDumping in_process_stack_dumping, TracingController tracing_controller) {
        DefaultPlatform platform = new DefaultPlatform(idle_task_support, tracing_controller);
        platform.setThreadPoolSize(thread_pool_size);
        platform.ensureBackgroundTaskRunnerInitialized();
        return platform;
    }

    public static Platform newDefaultPlatform() {
        return newDefaultPlatform(0, IdleTaskSupport.kDisabled, InProcessStackDumping.kDisabled, null);
    }

    public static abstract class StackTracePrinter implements Base.Method {
        public abstract void invoke();
    }

    public abstract Platform.StackTracePrinter getStackTracePrinter();
    
    public abstract PageAllocator getPageAllocator();
    
    public static enum IdleTaskSupport {
        kEnabled, kDisabled
    }

    public static enum InProcessStackDumping {
        kEnabled, kDisabled
    }

    public static StackTracePrinter PrintStackTrace = new StackTracePrinter() {
        @Override
        public void invoke() {

        }
    };

    public static DefaultPlatform.TimeFunction DefaultTimeFunction = new DefaultPlatform.TimeFunction() {
        @Override
        public double invoke() {
            return 0;
        }
    };
    
    public void onCriticalMemoryPressure() {
    	
	}
}
