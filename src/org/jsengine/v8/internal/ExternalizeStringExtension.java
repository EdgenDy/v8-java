package org.jsengine.v8.internal;

import org.jsengine.v8.Extension;

public class ExternalizeStringExtension extends Extension {
	private static String kSource = null;
	public ExternalizeStringExtension() {
		super("v8/externalize", kSource);
	}
}