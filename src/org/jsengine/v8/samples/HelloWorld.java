package org.jsengine.v8.samples;

import org.jsengine.v8.Platform;
import org.jsengine.v8.V8;
import org.jsengine.v8.Internal;

public class HelloWorld {
    public static void main(String[] args) {
        Platform platform = Platform.newDefaultPlatform();
        V8.initializePlatform(platform);
        System.out.println(platform);
        System.out.println(Internal.flags.length);
        System.out.println(Internal.num_flags);
    }
}
