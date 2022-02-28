package org.jsengine.v8;

import org.jsengine.Globals;

// include\v8.h:8888
public class V8 {
	// include\v8.h:8934
	// src\api\api.cc:5568
	public static boolean initialize() {
		org.jsengine.v8.internal.V8.initialize();
		if(Globals.v8_use_external_startup_data) {
			// org.jsengine.v8.Internal.readNatives();
		}
		return true;
	}
    // include\v8.h:9007
    // src\api\api.cc:5562
    public static void initializePlatform(Platform platform) {
        org.jsengine.v8.internal.V8.initializePlatform(platform);
    }
}
