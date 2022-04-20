package org.jsengine.v8.internal;

import org.jsengine.v8.Internal;
// src\init\bootstrapper.h:44
public class Bootstrapper {
    // src\init\bootstrapper.h:46
    // src\init\bootstrapper.cc:132
    public static void initializeOncePerProcess() {
		org.jsengine.V8.registerExtension(new FreeBufferExtension());
		org.jsengine.V8.registerExtension(new GCExtension(Internal.GCFunctionName()));
		org.jsengine.V8.registerExtension(new ExternalizeStringExtension());
		org.jsengine.V8.registerExtension(new StatisticsExtension());
		org.jsengine.V8.registerExtension(new TriggerFailureExtension());
		org.jsengine.V8.registerExtension(new IgnitionStatisticsExtension());
		
		if (Internal.isValidCpuTraceMarkFunctionName()) {
			org.jsengine.V8.registerExtension(new CpuTraceMarkExtension(Internal.FLAG_expose_cputracemark_as.getValue()));
		}
    }
}