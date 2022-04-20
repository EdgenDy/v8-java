package org.jsengine.v8.internal;

import org.jsengine.v8.Extension;

public class FreeBufferExtension extends Extension {
	public FreeBufferExtension() {
		super("v8/free-buffer", "native function freeBuffer();");
	}
}