package org.jsengine.v8.internal;

import org.jsengine.v8.Extension;

public class StatisticsExtension extends Extension {
	private static String kSource = null;
	public StatisticsExtension() {
		super("v8/statistics", kSource);
	}
}