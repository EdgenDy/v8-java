package org.jsengine.v8.platform.tracing;

import org.jsengine.v8.Base;
import org.jsengine.v8.base.Mutex;
import org.jsengine.v8.base.MutexGuard;
import org.jsengine.v8.platform.Tracing;

import java.util.Set;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

// include\libplatform\v8-tracing.h:229
public class TracingController extends org.jsengine.v8.TracingController {
    private TraceBuffer trace_buffer_;
    private TraceConfig trace_config_ = new TraceConfig();
    private Mutex mutex_;
    private HashSet<org.jsengine.v8.TracingController.TraceStateObserver> observers_ =
                new HashSet<org.jsengine.v8.TracingController.TraceStateObserver>();
    private AtomicBoolean recording_ = new AtomicBoolean(false);

	public static enum CategoryGroupEnabledFlags {
		ENABLED_FOR_RECORDING(1 << 0),
		ENABLED_FOR_EVENT_CALLBACK(1 << 2),
		ENABLED_FOR_ETW_EXPORT(1 << 3);
		
		private int value_;
		CategoryGroupEnabledFlags(int value) {
			this.value_ = value;
		}
		
		public int value() {
			return this.value_;
		}
    }

    public void initialize(TraceBuffer trace_buffer) {
        this.trace_buffer_ = trace_buffer;
        this.mutex_ = new Mutex();
    }
    
    protected void updateCategoryGroupEnabledFlag(int category_index) {
    	int enabled_flag = 0;
		String category_group = Tracing.g_category_groups[category_index];
		//System.out.println("TracingController.category_group = " + category_group);
		if (recording_.get() && trace_config_.isCategoryGroupEnabled(category_group)) {
			enabled_flag |= CategoryGroupEnabledFlags.ENABLED_FOR_RECORDING.value();
		}
		
		if (recording_.get() && category_group == "__metadata") {
			enabled_flag |= CategoryGroupEnabledFlags.ENABLED_FOR_RECORDING.value();
		}
		
		Tracing.g_category_group_enabled[category_index] = enabled_flag;
    }
    
    public int getCategoryGroupEnabled(String category_group) {
    	int category_index = Base.acquireLoad(Tracing.g_category_index);
    	//System.out.println("TracingController.category_index = " + category_index);
    
    	for (int i = 0; i < category_index; ++i) {
			if (Tracing.g_category_groups[i] == category_group) {
				return i;
			}
		}
		
		MutexGuard lock = new MutexGuard(mutex_);
		int category_group_enabled = 0;
		category_index = Base.acquireLoad(Tracing.g_category_index);
		for (int i = 0; i < category_index; ++i) {
			if (Tracing.g_category_groups[i] == category_group) {
				return i;
			}
		}
		
		if(category_index < Tracing.kMaxCategoryGroups) {
			String new_group = category_group;
			Tracing.g_category_groups[category_index] = new_group;
			updateCategoryGroupEnabledFlag(category_index);
			
			category_group_enabled = Tracing.g_category_group_enabled[category_index];
			Tracing.g_category_index.set(category_index + 1);
		}
		else {
			category_group_enabled = Tracing.g_category_group_enabled[Tracing.g_category_categories_exhausted];
		}
		lock.destroy();
    	return category_group_enabled; // for implementation
    }
    
    // include\libplatform\v8-tracing.h:277
    // include\src\libplatform\tracing-controller.cc:437
    public void addTraceStateObserver(org.jsengine.v8.TracingController.TraceStateObserver observer) {
    	MutexGuard guard = new MutexGuard(mutex_);
        observers_.add(observer);
        
        if(!recording_.get()) {
        	guard.destroy();
        	return;
        }
        observer.OnTraceEnabled();
    }
}
