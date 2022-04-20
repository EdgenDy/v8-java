package org.jsengine.v8;

public class ArrayBuffer extends Object$ {
	public static class Allocator {
		public static Allocator newDefaultAllocator() {
			return new ArrayBufferAllocator();
		}
	}
}