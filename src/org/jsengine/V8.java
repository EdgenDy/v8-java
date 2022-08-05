package org.jsengine;

import org.jsengine.v8.Extension;
import org.jsengine.v8.RegisteredExtension;

public class V8 {
	public static void registerExtension(Extension extension) {
		RegisteredExtension.register(extension);
	}
	
	public static enum MemoryPressureLevel {
		kNone,
		kModerate,
		kCritical
	};
}