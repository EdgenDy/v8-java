package org.jsengine.v8;


public abstract class TracingController {
    public static abstract class TraceStateObserver {
        public abstract void OnTraceEnabled();
        public abstract void OnTraceDisabled();
    }
    
    public abstract void addTraceStateObserver(org.jsengine.v8.TracingController.TraceStateObserver observer);
    public abstract int getCategoryGroupEnabled(String category_group);
    protected abstract void updateCategoryGroupEnabledFlag(int category_index);
}
