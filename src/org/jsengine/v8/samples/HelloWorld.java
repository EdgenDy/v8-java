package org.jsengine.v8.samples;

import org.jsengine.v8.V8;
import org.jsengine.v8.Isolate;
import org.jsengine.v8.Platform;
import org.jsengine.v8.Internal;
import org.jsengine.v8.ArrayBuffer;

public class HelloWorld {
    public static void main(String[] args) {
        Platform platform = Platform.newDefaultPlatform();
        V8.initializePlatform(platform);
        V8.initialize();
        
        Isolate.CreateParams create_params = new Isolate.CreateParams();
        create_params.array_buffer_allocator = ArrayBuffer.Allocator.newDefaultAllocator();
        Isolate isolate = Isolate.newInstance(create_params);
    }
}
