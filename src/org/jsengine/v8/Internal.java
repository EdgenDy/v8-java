package org.jsengine.v8;

import org.jsengine.v8.Base;
import org.jsengine.v8.PageAllocator;
import org.jsengine.v8.internal.Flag;
import org.jsengine.v8.internal.PageAllocatorInitializer;
import org.jsengine.v8.base.LeakyObject;

import org.jsengine.utils.Var;

// class representation of v8::internal namespace to hold static methods

public class Internal {
	public static Base.OnceType init_once = new Base.OnceType();
	
	public static Flag flags[];
	public static int num_flags;
	
	public static Var<Boolean> FLAG_use_strict = new Var<Boolean>(false);
	public static Var<Boolean> FLAGDEFAULT_use_strict = new Var<Boolean>(false);
	
	public static Var<Boolean> FLAG_es_staging = new Var<Boolean>(false);
	public static Var<Boolean> FLAGDEFAULT_es_staging = new Var<Boolean>(false);
	
	public static Var<Boolean> FLAG_harmony = new Var<Boolean>(false);
	public static Var<Boolean> FLAGDEFAULT_harmony = new Var<Boolean>(false);
	
	public static Var<Boolean> FLAG_harmony_shipping = new Var<Boolean>(false);
	public static Var<Boolean> FLAGDEFAULT_harmony_shipping = new Var<Boolean>(false);
	
	public static Var<Boolean> FLAG_predictable = new Var<Boolean>(false);
	public static Var<Boolean> FLAGDEFAULT_predictable = new Var<Boolean>(false);
	
	public static Var<Boolean> FLAG_stress_compaction = new Var<Boolean>(false);
	public static Var<Boolean> FLAGDEFAULT_stress_compaction = new Var<Boolean>(false);
	
	public static Var<Boolean> FLAG_force_marking_deque_overflows = new Var<Boolean>(false);
	public static Var<Boolean> FLAGDEFAULT_force_marking_deque_overflows = new Var<Boolean>(false);
	
	public static Var<Boolean> FLAG_gc_global = new Var<Boolean>(false);
	public static Var<Boolean> FLAGDEFAULT_gc_global = new Var<Boolean>(false);
	
	public static Var<Boolean> FLAG_trace_turbo = new Var<Boolean>(false);
	public static Var<Boolean> FLAGDEFAULT_trace_turbo = new Var<Boolean>(false);
	
	public static Var<Boolean> FLAG_jitless = new Var<Boolean>(false);
	public static Var<Boolean> FLAGDEFAULT_jitless = new Var<Boolean>(false);
	
	public static Var<Boolean> FLAG_correctness_fuzzer_suppressions = new Var<Boolean>(false);
	public static Var<Boolean> FLAGDEFAULT_correctness_fuzzer_suppressions = new Var<Boolean>(false);
	
	public static Var<Boolean> FLAG_expose_wasm = new Var<Boolean>(true);
	public static Var<Boolean> FLAGDEFAULT_expose_wasm = new Var<Boolean>(true);
	
	public static Var<Boolean> FLAG_interpreted_frames_native_stack = new Var<Boolean>(false);
	public static Var<Boolean> FLAGDEFAULT_interpreted_frames_native_stack = new Var<Boolean>(false);
	
	public static Var<Boolean> FLAG_hard_abort = new Var<Boolean>(true);
	public static Var<Boolean> FLAGDEFAULT_hard_abort = new Var<Boolean>(true);
	
	
	public static Var<Integer> FLAG_random_seed = new Var<Integer>(0);
	public static Var<Integer> FLAGDEFAULT_random_seed = new Var<Integer>(0);
	
	public static Var<Integer> FLAG_max_semi_space_size = new Var<Integer>(0);
	public static Var<Integer> FLAGDEFAULT_max_semi_space_size = new Var<Integer>(0);
	
	public static Var<String> FLAG_trace_turbo_cfg_file = new Var<String>(null);
	public static Var<String> FLAGDEFAULT_trace_turbo_cfg_file = new Var<String>(null);
	
	public static Var<String> FLAG_gc_fake_mmap = new Var<String>("/tmp/__v8_gc__");
	public static Var<String> FLAGDEFAULT_gc_fake_mmap = new Var<String>("/tmp/__v8_gc__");
	
	public static final boolean FLAG_embedded_builtins = true;
	public static Var<Boolean> FLAG_profile_deserialization = new Var<Boolean>(false);
	public static Var<Boolean> FLAGDEFAULT_profile_deserialization = new Var<Boolean>(false);
	
	static {
		flags = new Flag[] {
			new Flag(Flag.FlagType.TYPE_BOOL, "use_strict", FLAG_use_strict, FLAGDEFAULT_use_strict, "enforce strict mode", false),
			new Flag(Flag.FlagType.TYPE_BOOL, "es_staging", FLAG_es_staging, FLAGDEFAULT_es_staging,"enable test-worthy harmony features (for internal use only)", false),
			new Flag(Flag.FlagType.TYPE_BOOL, "harmony", FLAG_harmony, FLAGDEFAULT_harmony, "enable all completed harmony features", false),
			new Flag(Flag.FlagType.TYPE_BOOL, "harmony", FLAG_harmony, FLAGDEFAULT_harmony_shipping, "enable all shipped harmony features", false),
			new Flag(Flag.FlagType.TYPE_BOOL, "profile_deserialization", FLAG_profile_deserialization, FLAGDEFAULT_profile_deserialization, "Print the time it takes to deserialize the snapshot.", false),
			new Flag(Flag.FlagType.TYPE_BOOL, "predictable", FLAG_predictable, FLAGDEFAULT_predictable, "enable predictable mode", false),
			new Flag(Flag.FlagType.TYPE_INT, "random_seed", FLAG_random_seed, FLAGDEFAULT_random_seed, "Default seed for initializing random generator (0, the default, means to use system random).", false),
			new Flag(Flag.FlagType.TYPE_BOOL, "stress_compaction", FLAG_stress_compaction, FLAGDEFAULT_stress_compaction, "stress the GC compactor to flush out bugs (implies --force_marking_deque_overflows)", false),
			new Flag(Flag.FlagType.TYPE_BOOL, "force_marking_deque_overflows", FLAG_force_marking_deque_overflows, FLAGDEFAULT_force_marking_deque_overflows, "force overflows of marking deque by reducing it's size to 64 words", false),
			new Flag(Flag.FlagType.TYPE_BOOL, "gc_global", FLAG_gc_global, FLAGDEFAULT_gc_global, "always perform global GCs", false),
			new Flag(Flag.FlagType.TYPE_SIZE_T, "max_semi_space_size", FLAG_max_semi_space_size, FLAGDEFAULT_max_semi_space_size, "max size of the old space (in Mbytes)", false),
			new Flag(Flag.FlagType.TYPE_BOOL, "trace_turbo", FLAG_trace_turbo, FLAGDEFAULT_trace_turbo, "trace generated TurboFan IR", false),
			new Flag(Flag.FlagType.TYPE_STRING, "trace_turbo_cfg_file", FLAG_trace_turbo_cfg_file, FLAGDEFAULT_trace_turbo_cfg_file, "trace turbo cfg graph (for C1 visualizer) to a given file name", false),
			new Flag(Flag.FlagType.TYPE_BOOL, "jitless", FLAG_jitless, FLAGDEFAULT_jitless, "Disable runtime allocation of executable memory.", false),
			new Flag(Flag.FlagType.TYPE_BOOL, "correctness_fuzzer_suppressions", FLAG_correctness_fuzzer_suppressions, FLAGDEFAULT_correctness_fuzzer_suppressions, "Suppress certain unspecified behaviors to ease correctness fuzzing: Abort program when the stack overflows or a string exceeds maximum length (as opposed to throwing RangeError). Use a fixed suppression string for error messages.", false),
			new Flag(Flag.FlagType.TYPE_BOOL, "expose_wasm", FLAG_expose_wasm, FLAGDEFAULT_expose_wasm, "expose wasm interface to JavaScript", false),
			new Flag(Flag.FlagType.TYPE_BOOL, "interpreted_frames_native_stack", FLAG_interpreted_frames_native_stack, FLAGDEFAULT_interpreted_frames_native_stack, "Show interpreted frames on the native stack (useful for external profilers).", false),
			new Flag(Flag.FlagType.TYPE_BOOL, "hard_abort", FLAG_hard_abort, FLAGDEFAULT_hard_abort, "abort by crashing", false),
			new Flag(Flag.FlagType.TYPE_BOOL, "gc_fake_mmap", FLAG_gc_fake_mmap, FLAGDEFAULT_gc_fake_mmap, "Specify the name of the file for fake gc mmap used in ll_prof", false)
		};
		num_flags = flags.length;
	}
	
	public static int flag_hash = 0;
	
	public static void computeFlagListHash() {
		StringBuilder modified_args_as_string = new StringBuilder();
		if (FLAG_embedded_builtins) {
			modified_args_as_string.append("embedded");
		}
		
		for (int i = 0; i < num_flags; ++i) {
			Flag current = flags[i];
			if (current.type() == Flag.FlagType.TYPE_BOOL &&
				current.bool_variable().equals(FLAG_profile_deserialization)) {
				continue;
			}
			if (!current.isDefault()) {
				modified_args_as_string.append(i);
				modified_args_as_string.append(current.hashCode());
			}
		}
		
		String args = new String(modified_args_as_string.toString());
		flag_hash = Base.hash_range(args.hashCode(), args.hashCode() + args.length());
	}
	
	public static void setRandomMmapSeed(int seed) {
		getPlatformPageAllocator().setRandomMmapSeed(seed);
	}
	public static LeakyObject<PageAllocatorInitializer> object;
	public static PageAllocatorInitializer getPageTableInitializer() {
		if(object == null) object = new LeakyObject<PageAllocatorInitializer>(new PageAllocatorInitializer());
		return object.get();
	}
	
	public static PageAllocator getPlatformPageAllocator() {
		return getPageTableInitializer().page_allocator();
	}
}