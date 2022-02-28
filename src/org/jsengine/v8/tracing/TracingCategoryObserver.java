package org.jsengine.v8.tracing;

import org.jsengine.v8.internal.TracingFlags;
import org.jsengine.v8.internal.V8;
import org.jsengine.v8.internal.tracing.TraceEventHelper;
import org.jsengine.v8.TracingController;
import org.jsengine.v8.Base;
import org.jsengine.v8.base.AtomicWord;
import org.jsengine.Globals;

import org.jsengine.utils.Var;

// src\tracing\tracing-category-observer.h:13
public class TracingCategoryObserver extends TracingController.TraceStateObserver {
    private static TracingCategoryObserver instance_;
    public static enum Mode {
        ENABLED_BY_NATIVE(1 << 0),
        ENABLED_BY_TRACING(1 << 1),
        ENABLED_BY_SAMPLING(1 << 2);

        private Mode(int value) {
            value_ = value;
        }

        private int value_;

        public int value() {
            return value_;
        }
    }

 // src\tracing\tracing-category-observer.h:21
 // src\tracing\tracing-category-observer.cc
    public static void setUp() {
        TracingCategoryObserver.instance_ = new TracingCategoryObserver();
        V8.getCurrentPlatform().getTracingController()
			.addTraceStateObserver(TracingCategoryObserver.instance_);
    }
 // src\tracing\tracing-category-observer.h:32
    public static void tearDown() {

    }
 // src\tracing\tracing-category-observer.h:25
 // src\tracing\tracing-category-observer.cc:29
    @Override
    public void OnTraceEnabled() {
		boolean enabled = false;
        do {
        	AtomicWord trace_event_unique_atomic52 = new AtomicWord(0);
        	int trace_event_unique_category_group_enabled52;
        	
        	trace_event_unique_category_group_enabled52 = Base.relaxedLoad(trace_event_unique_atomic52);
        	
        	if(trace_event_unique_category_group_enabled52 == 0) {
        		trace_event_unique_category_group_enabled52 = TraceEventHelper.getTracingController().getCategoryGroupEnabled("disabled-by-default-v8.runtime_stats");
        		trace_event_unique_atomic52.set(trace_event_unique_category_group_enabled52);
        	}
        	
        	if((trace_event_unique_category_group_enabled52 & (Globals.CategoryGroupEnabledFlags.kEnabledForRecording_CategoryGroupEnabledFlags.value() | Globals.CategoryGroupEnabledFlags.kEnabledForEventCallback_CategoryGroupEnabledFlags.value())) != 0)
        		enabled = true;
        	else
        		enabled = false;
        	
        } while(false);

		if(enabled) {
			TracingFlags.runtime_stats.set(TracingFlags.runtime_stats.get() | Mode.ENABLED_BY_TRACING.value());
		}
		
		do {
        	AtomicWord trace_event_unique_atomic52 = new AtomicWord(0);
        	int trace_event_unique_category_group_enabled52;
        	
        	trace_event_unique_category_group_enabled52 = Base.relaxedLoad(trace_event_unique_atomic52);
        	
        	if(trace_event_unique_category_group_enabled52 == 0) {
        		trace_event_unique_category_group_enabled52 = TraceEventHelper.getTracingController().getCategoryGroupEnabled("disabled-by-default-v8.runtime_stats_sampling");
        		trace_event_unique_atomic52.set(trace_event_unique_category_group_enabled52);
        	}
        	
        	if((trace_event_unique_category_group_enabled52 & (Globals.CategoryGroupEnabledFlags.kEnabledForRecording_CategoryGroupEnabledFlags.value() | Globals.CategoryGroupEnabledFlags.kEnabledForEventCallback_CategoryGroupEnabledFlags.value())) != 0)
        		enabled = true;
        	else
        		enabled = false;
        	
        } while(false);
			
		if(enabled) {
			TracingFlags.runtime_stats.set(TracingFlags.runtime_stats.get() | Mode.ENABLED_BY_SAMPLING.value());
		}
		
		do {
        	AtomicWord trace_event_unique_atomic52 = new AtomicWord(0);
        	int trace_event_unique_category_group_enabled52;
        	
        	trace_event_unique_category_group_enabled52 = Base.relaxedLoad(trace_event_unique_atomic52);
        	
        	if(trace_event_unique_category_group_enabled52 == 0) {
        		trace_event_unique_category_group_enabled52 = TraceEventHelper.getTracingController().getCategoryGroupEnabled("disabled-by-default-v8.gc");
        		trace_event_unique_atomic52.set(trace_event_unique_category_group_enabled52);
        	}
        	
        	if((trace_event_unique_category_group_enabled52 & (Globals.CategoryGroupEnabledFlags.kEnabledForRecording_CategoryGroupEnabledFlags.value() | Globals.CategoryGroupEnabledFlags.kEnabledForEventCallback_CategoryGroupEnabledFlags.value())) != 0)
        		enabled = true;
        	else
        		enabled = false;
        	
        } while(false);
		
		
		if(enabled) {
			TracingFlags.gc.set(TracingFlags.gc.get() | Mode.ENABLED_BY_TRACING.value());
		}
		
		do {
        	AtomicWord trace_event_unique_atomic52 = new AtomicWord(0);
        	int trace_event_unique_category_group_enabled52;
        	
        	trace_event_unique_category_group_enabled52 = Base.relaxedLoad(trace_event_unique_atomic52);
        	
        	if(trace_event_unique_category_group_enabled52 == 0) {
        		trace_event_unique_category_group_enabled52 = TraceEventHelper.getTracingController().getCategoryGroupEnabled("disabled-by-default-v8.gc_stats");
        		trace_event_unique_atomic52.set(trace_event_unique_category_group_enabled52);
        	}
        	
        	if((trace_event_unique_category_group_enabled52 & (Globals.CategoryGroupEnabledFlags.kEnabledForRecording_CategoryGroupEnabledFlags.value() | Globals.CategoryGroupEnabledFlags.kEnabledForEventCallback_CategoryGroupEnabledFlags.value())) != 0)
        		enabled = true;
        	else
        		enabled = false;
        	
        } while(false);
			
		if(enabled) {
			TracingFlags.gc_stats.set(TracingFlags.gc_stats.get() | Mode.ENABLED_BY_TRACING.value());
		}
		
		do {
        	AtomicWord trace_event_unique_atomic52 = new AtomicWord(0);
        	int trace_event_unique_category_group_enabled52;
        	
        	trace_event_unique_category_group_enabled52 = Base.relaxedLoad(trace_event_unique_atomic52);
        	
        	if(trace_event_unique_category_group_enabled52 == 0) {
        		trace_event_unique_category_group_enabled52 = TraceEventHelper.getTracingController().getCategoryGroupEnabled("disabled-by-default-v8.ic_stats");
        		trace_event_unique_atomic52.set(trace_event_unique_category_group_enabled52);
        	}
        	
        	if((trace_event_unique_category_group_enabled52 & (Globals.CategoryGroupEnabledFlags.kEnabledForRecording_CategoryGroupEnabledFlags.value() | Globals.CategoryGroupEnabledFlags.kEnabledForEventCallback_CategoryGroupEnabledFlags.value())) != 0)
        		enabled = true;
        	else
        		enabled = false;
        	
        } while(false);
			
		if(enabled) {
			TracingFlags.ic_stats.set(TracingFlags.ic_stats.get() | Mode.ENABLED_BY_TRACING.value());
		}
    }
 // src\tracing\tracing-category-observer.h:26
    @Override
    public void OnTraceDisabled() {

    }
}
