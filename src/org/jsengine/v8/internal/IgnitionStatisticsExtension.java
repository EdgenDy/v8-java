package org.jsengine.v8.internal;

import org.jsengine.v8.Extension;

public class IgnitionStatisticsExtension extends Extension {
	private static String kSource = null;
	public IgnitionStatisticsExtension() {
		super("v8/trigger-failure", kSource);
	}
}