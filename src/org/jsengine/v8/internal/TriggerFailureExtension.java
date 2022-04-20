package org.jsengine.v8.internal;

import org.jsengine.v8.Extension;

public class TriggerFailureExtension extends Extension {
	private static String kSource = null;
	public TriggerFailureExtension() {
		super("v8/trigger-failure", kSource);
	}
}