package org.jsengine;

import org.jsengine.utils.Var;
import org.jsengine.utils.Pointer;
import org.jsengine.v8.Base;
import org.jsengine.v8.base.AtomicWord;
import org.jsengine.v8.internal.tracing.TraceEventHelper;
import org.jsengine.Memory;

import java.util.HashMap;
import java.util.Scanner;
import java.io.File;


public class Globals {
	// on a 64 bit machine (windows)
	public static int kSystemPointerSize = 8;
	public static HashMap<String, Object> variables = new HashMap<String, Object>();
	
	public static enum CategoryGroupEnabledFlags {
		kEnabledForRecording_CategoryGroupEnabledFlags(1 << 0),
		kEnabledForEventCallback_CategoryGroupEnabledFlags(1 << 2),
		kEnabledForETWExport_CategoryGroupEnabledFlags(1 << 3);
		
		int value_;
		CategoryGroupEnabledFlags(int value) {
			value_ = value;
		}
		
		public int value() {
			return value_;
		}
	};
	
	public static void trace_event_category_group_enabled(String category_group, Var<Boolean> ret) {
		do {                                                                      
			internal_trace_event_get_category_info(category_group);
			if (internal_trace_event_category_group_enabled_for_recording_mode() > 0) {
				ret.setValue(true);
			}
			else {                                                                
				ret.setValue(false);                                                  
			}                                                        
		} while (false);
	}
	
	public static void internal_trace_event_get_category_info(String category_group) {
		variables.put(internal_trace_event_uid("atomic"), new AtomicWord(0));
		variables.put(internal_trace_event_uid("category_group_enabled"), null);
		internal_trace_event_get_category_info_custom_variables(category_group,
			internal_trace_event_uid("atomic"),
			internal_trace_event_uid("category_group_enabled"));
	}
	
	public static String trace_disabled_by_default(String name) {
		return "disabled-by-default-" + name;
	}
	
	public static String internal_trace_event_uid(String name_prefix) {
		return internal_trace_event_uid2(name_prefix, "__LINE__");
	}
	
	public static String internal_trace_event_uid2(String a, String b) {
		return internal_trace_event_uid3(a, b);
	}
	
	public static String internal_trace_event_uid3(String a, String b) {
		return "trace_event_unique_" + a + b;
	}
	
	public static void internal_trace_event_get_category_info_custom_variables(String category_group, String atomic, String category_group_enabled) {
		variables.put(category_group_enabled, new AtomicWord(trace_event_api_atomic_load((AtomicWord) variables.get(atomic))));
		if(((AtomicWord) variables.get(category_group_enabled)).get() == 0) {
			variables.put(category_group_enabled, new AtomicWord(trace_event_api_get_category_group_enabled(category_group)));
			((AtomicWord) variables.get(atomic)).set(((AtomicWord)variables.get(category_group_enabled)).get());
		}
	}   
	
	public static int internal_trace_event_category_group_enabled_for_recording_mode() {
		return trace_event_api_load_category_group_enabled() &
      (CategoryGroupEnabledFlags.kEnabledForRecording_CategoryGroupEnabledFlags.value() | CategoryGroupEnabledFlags.kEnabledForEventCallback_CategoryGroupEnabledFlags.value());
	}
	
	public static int trace_event_api_load_category_group_enabled() {
		return Base.relaxedLoad((AtomicWord) variables.get(internal_trace_event_uid("category_group_enabled")));
	}
	
	public static int trace_event_api_atomic_load(AtomicWord var) {
		return Base.relaxedLoad(var);
	}
	
	public static void trace_event_api_atomic_store(AtomicWord var, int value) {
		var.set(value);
	}
	
	public static int trace_event_api_get_category_group_enabled(String category_group) {
		return TraceEventHelper.getTracingController().getCategoryGroupEnabled(category_group);
	}
	
	public static boolean v8_use_external_startup_data = false;
	
	public static boolean V8_EMBEDDED_BUILTINS = true;
	
	public static class Handle {
		
	}
	
	public static class Dword extends Var<Integer> {
		public Dword(int value) {
			super(value);
		}
	}
	
	private static int tlsAllocs = 5;
	public static Dword tlsAlloc() {
		return new Dword(tlsAllocs++);
	}
	
	public static class Std {
		public static void swap(int[] n0, int[] n1, int index0, int index1) {
			int temp = n0[index0];
			n0[index0] = n1[index1];
			n1[index1] = temp;
		}
	}
	
	public static char[] memcpy(char[] dest, String source, int num) {
		char[] result = source.toCharArray();
		for(int index = 0, limit = num; index < limit; index++) {
			dest[index] = result[index];
		}
		return dest;
	}
	
	public static char[] memcpy(char[] dest, int ...nums) {
		for(int index = 0, limit = nums.length; index < limit; index++) {
			int num = nums[index];
			char b0 = (char) (num & 255);
			char b1 = (char) ((num >> 8) & 255);
			char b2 = (char) ((num >> 16) & 255);
			char b3 = (char) (num >> 24);
			
			dest[(index * 4)] = b0;
			dest[(index * 4) + 1] = b1;
			dest[(index * 4) + 2] = b2;
			dest[(index * 4) + 3] = b3;
			
		}
		return dest;
	}
	
	public static void __cpuid(int cpu_info[], int info_type) {
		switch(info_type) {
			case 0: {
				cpu_info[0] = 13;
				cpu_info[1] = 1970169159;
				cpu_info[2] = 1818588270;
				cpu_info[3] = 1231384169;
				break;
			}
			
			case 1: {
				cpu_info[0] = 67194;
				cpu_info[1] = 16910336;
				cpu_info[2] = 201384861;
				cpu_info[3] = -1075053569;
				break;
			}
			
			case 7: {
				cpu_info[0] = 0;
				cpu_info[1] = 0;
				cpu_info[2] = 0;
				cpu_info[3] = 0;
				break;
			}
			
			case 0x80000000: {
				cpu_info[0] = -2147483640;
				cpu_info[1] = 0;
				cpu_info[2] = 0;
				cpu_info[3] = 0;
				break;
			}
			
			case 0x80000001: {
				cpu_info[0] = 0;
				cpu_info[1] = 0;
				cpu_info[2] = 0;
				cpu_info[3] = 537921536;
				break;
			}
			
			case 0x80000007: {
				cpu_info[0] = 0;
				cpu_info[1] = 0;
				cpu_info[2] = 0;
				cpu_info[3] = 0;
				break;
			}
		}
	}
	
	public static int _vsnprintf_s(char[] buffer, int sizeOfBuffer, int count, String format, Object ...argptr) {
		String result = String.format(format, argptr);
		char[] array = result.toCharArray();
		
		if(buffer.length < array.length) {
			return buffer.length - array.length;
		}
		else {
			for(int index = 0, limit = array.length; index < limit; index++) {
				buffer[index] = array[index];
			}
		}
		return (buffer.length - array.length);
	}
	
	public static int kPageSizeBits = 18;
	
	
	public static int roundDown(int x, int m) {
		return x & -m;
	}
	
	public static long roundDown(long x, long m) {
		return x & -m;
	}
	
	public static long roundUp(long x, long m) {
		return roundDown(x + m - 1, m);
	}
	
	public static Pointer<Object> nullptr = new Pointer<Object>(0);
	
	public static Pointer<Object> alignedAddress(Pointer<Object> address, long alignment) {
		return new Pointer<Object>(address.getValue() & ~(alignment - 1));
	}
	
	public static long MEM_RESERVE = 0x00100L;
	public static long MEM_COMMIT = 0x00200L;
	public static long MEM_DECOMMIT = 0x00300L;
	public static long MEM_RELEASE = 0x00400L;
	public static long PAGE_NOACCESS = 0x00500L;
	public static long PAGE_READONLY = 0x00600L;
	public static long PAGE_READWRITE = 0x00700L;
	public static long PAGE_EXECUTE_READWRITE = 0x00800L;
	public static long PAGE_EXECUTE_READ = 0x00900L;
	
	// windows builtin functions for allocation on the virtual memory.
	public static Pointer<Object> virtualAlloc(Pointer<Object> hint, long size, long flags, long protect) {
		if (Globals.MEM_RESERVE == flags)
			return Memory.reserve(hint.getValue(), size);
		
		if (Globals.MEM_COMMIT == flags)
			return Memory.commit(hint.getValue(), size);
		
		System.out.println("Globals::virtualAlloc : unknown flag");
		return nullptr;
	}
	
	public static boolean virtualFree(Pointer<Object> address, long size, long flags) {
		return Memory.free(address.getValue(), size);
	}
	
	public static String readFile(String path) {
		try {
			Scanner scanner = new Scanner(new File(path));
			StringBuilder content = new StringBuilder();
			if (scanner.hasNext()) {
				content.append(scanner.nextLine());
				if (scanner.hasNext())
					content.append("\n");
			}
			
			return content.toString();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}