package org.jsengine.v8.internal;

import org.jsengine.v8.Internal;

// src/flags/flags.h
public class FlagList {
	public static void enforceFlagImplications() {
		Internal.computeFlagListHash();
	}
}