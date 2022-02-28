package org.jsengine.v8.platform;

import org.jsengine.v8.Platform;
import org.jsengine.v8.base.SysInfo;
import org.jsengine.v8.base.Mutex;
import org.jsengine.v8.base.MutexGuard;

import org.jsengine.v8.TracingController;
import org.jsengine.v8.PageAllocator;

public class DefaultPlatform extends Platform {
    private static final int kMaxThreadPoolSize = 8;

    private int thread_pool_size_;
    private IdleTaskSupport idle_task_support_;

    private TracingController tracing_controller_;
    private PageAllocator page_allocator_;
    
    private Mutex lock_ = new Mutex();
    private TimeFunction time_function_for_testing_ = null;
    private DefaultWorkerThreadsTaskRunner worker_threads_task_runner_ = null;


    public DefaultPlatform(IdleTaskSupport idle_task_support, org.jsengine.v8.TracingController tracing_controller) {
        this.thread_pool_size_ = 0;
        this.idle_task_support_ = idle_task_support;
        this.tracing_controller_ = tracing_controller;
        this.page_allocator_ = new org.jsengine.v8.base.PageAllocator();
        this.time_function_for_testing_ = null;

        if(tracing_controller == null) {
            org.jsengine.v8.platform.tracing.TracingController controller = new org.jsengine.v8.platform.tracing.TracingController();
            controller.initialize(null);
            tracing_controller_ = controller;
        }
    }

    public void setThreadPoolSize(int thread_pool_size) {
    	MutexGuard guard = new MutexGuard(lock_);
        if (thread_pool_size < 1) {
            thread_pool_size = SysInfo.NumberOfProcessors() - 1;
        }
        thread_pool_size_ = Integer.max(Integer.min(thread_pool_size, DefaultPlatform.kMaxThreadPoolSize), 1);
        guard.destroy();
    }

    public TracingController getTracingController() {
        return tracing_controller_;
    }

    public static abstract class TimeFunction {
        public abstract double invoke();
    }

    public void ensureBackgroundTaskRunnerInitialized() {
		MutexGuard guard = new MutexGuard(lock_);
        if(worker_threads_task_runner_ == null) {
            worker_threads_task_runner_ = new DefaultWorkerThreadsTaskRunner(thread_pool_size_,
                    time_function_for_testing_ != null
                                    ? time_function_for_testing_ : Platform.DefaultTimeFunction);
        }
        guard.destroy();
    }

    public Platform.StackTracePrinter getStackTracePrinter() {
        return Platform.PrintStackTrace;
    }
    
    public PageAllocator getPageAllocator() {
    	return page_allocator_;
    }
}
