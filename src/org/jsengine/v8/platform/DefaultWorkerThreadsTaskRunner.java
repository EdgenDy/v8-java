package org.jsengine.v8.platform;

import org.jsengine.v8.TaskRunner;
import org.jsengine.v8.Task;

import java.util.Vector;

public class DefaultWorkerThreadsTaskRunner extends TaskRunner {
	private DelayedTaskQueue queue_;
	private DefaultPlatform.TimeFunction time_function;

	private Vector<WorkerThread> thread_pool_ = new Vector<WorkerThread>();
	private int thread_pool_size_;
	
    public DefaultWorkerThreadsTaskRunner(int thread_pool_size, DefaultPlatform.TimeFunction time_function) {
		this.queue_ = new DelayedTaskQueue(time_function);
		this.time_function = time_function;
		this.thread_pool_size_ = thread_pool_size;
		
		for(int i = 0; i < thread_pool_size; ++i) {
			thread_pool_.add(new WorkerThread(this));
		}
    }
    
    public void postTask(Task task) {

    }
    
    // src\libplatform\default-worker-threads-task-runner.h:48
    // src\libplatform\default-worker-threads-task-runner.cc:72
    public static class WorkerThread extends org.jsengine.v8.base.Thread {
    	private DefaultWorkerThreadsTaskRunner runner_;
    	public WorkerThread(DefaultWorkerThreadsTaskRunner runner) {
			super(new Options("V8 DefaultWorkerThreadsTaskRunner WorkerThread"));
    		this.runner_ = runner;
    	}

		@Override
		public void run() {

		}
	}
}
