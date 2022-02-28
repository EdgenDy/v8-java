package org.jsengine.v8.internal.tracing;

import org.jsengine.v8.TracingController;
import org.jsengine.v8.internal.V8;

public class TraceEventHelper {
	public static TracingController getTracingController() {
		return V8.getCurrentPlatform().getTracingController();
	}
}