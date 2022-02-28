package org.jsengine.v8.internal;

import java.util.concurrent.atomic.AtomicInteger;

// src\logging\counters.h:31
public class TracingFlags {
	public static AtomicInteger runtime_stats = new AtomicInteger(0);
	public static AtomicInteger gc = new AtomicInteger(0);
	public static AtomicInteger gc_stats = new AtomicInteger(0);
	public static AtomicInteger ic_stats = new AtomicInteger(0);

	public static boolean is_runtime_stats_enabled() {
    	return runtime_stats.get() != 0;
	}

	public static boolean is_gc_enabled() {
		return gc.get() != 0;
	}

	public static boolean is_gc_stats_enabled() {
		return gc_stats.get() != 0;
	}

	public static boolean is_ic_stats_enabled() {
		return ic_stats.get() != 0;
	}
}