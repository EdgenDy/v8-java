package org.jsengine;

import org.jsengine.utils.Pointer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Memory {
	private static ArrayList<Page> vmemory = new ArrayList<Page>();
	private static Map<Long, Page> memory_map = new HashMap<Long, Page>();
	private static long addr = 0x01; 
	
	public static Pointer<Object> allocate() {
		return allocate((Object) null);
	}
	
	public static <T> Pointer<T> allocate(T data) {
		memory_map.put(addr, new Page<T>(data));
		return new Pointer<T>(addr);
	}
	
	public static <T> void store(long address, T data) 
		Page<T> page = load(address);
		page.value = data;
	}
	
	public static Page load(long index) {
		return memory_map.get(index);
	}
	
	public static class Page<T> {
		public T value;
		public Page(T value) {
			this.value = value;
		}
	}
}