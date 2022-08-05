package org.jsengine.v8;

import org.jsengine.v8.Base;
import org.jsengine.v8.PageAllocator;
import org.jsengine.v8.internal.Flag;
import org.jsengine.v8.internal.PageAllocatorInitializer;
import org.jsengine.v8.internal.Vector;
import org.jsengine.v8.internal.Register;
import org.jsengine.v8.internal.RegisterCode;
import org.jsengine.v8.internal.CallInterfaceDescriptorData;
import org.jsengine.v8.internal.Address;
import org.jsengine.v8.base.LeakyObject;
import org.jsengine.v8.base.OS;
import org.jsengine.Globals;
import org.jsengine.utils.Pointer;
import org.jsengine.utils.Var;

import java.util.concurrent.atomic.AtomicInteger;

// class representation of v8::internal namespace to hold static methods and static variables

public class Internal {
	public static Base.OnceType init_once = new Base.OnceType();
	
	public static Address kNullAddress = new Address(0);
	
	public static Flag flags[];
	public static int num_flags;
	
	public static int KB = 1024;
	public static int MB = KB * KB;
	public static int GB = KB * KB * KB;

	public static int kUInt8Size = 1; // sizeof uint8_t
	public static int kUInt16Size = 2; // (uint16_t);

	public static int kDoubleSize = 8; // sizeof(double);

	public static int kMaxWasmCodeMB = 1024;
	public static int kInt32Size = 4;
	public static int kTaggedSize = kInt32Size;
	public static int kSystemPointerSize = 8; // 8 is sizeof(void*) in 64 bit windows
	
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
	
	public static Var<Boolean> FLAG_enable_sse4_2 = new Var<Boolean>(true);
	public static Var<Boolean> FLAGDEFAULT_enable_sse4_2 = new Var<Boolean>(true);
            
	public static Var<Boolean> FLAG_enable_sse4_1 = new Var<Boolean>(true);
	public static Var<Boolean> FLAGDEFAULT_enable_sse4_1 = new Var<Boolean>(true);

	public static Var<Boolean> FLAG_enable_sse3 = new Var<Boolean>(true);
	public static Var<Boolean> FLAGDEFAULT_enable_sse3 = new Var<Boolean>(true);
	
	public static Var<Boolean> FLAG_enable_ssse3 = new Var<Boolean>(true);
	public static Var<Boolean> FLAGDEFAULT_enable_ssse3 = new Var<Boolean>(true);

	public static Var<Boolean> FLAG_enable_sahf = new Var<Boolean>(true);
	public static Var<Boolean> FLAGDEFAULT_enable_sahf = new Var<Boolean>(true);    
	
	public static Var<Boolean> FLAG_enable_avx = new Var<Boolean>(true);
	public static Var<Boolean> FLAGDEFAULT_enable_avx = new Var<Boolean>(true); 

	public static Var<Boolean> FLAG_enable_fma3 = new Var<Boolean>(true);
	public static Var<Boolean> FLAGDEFAULT_enable_fma3 = new Var<Boolean>(true); 
	
	public static Var<Boolean> FLAG_enable_bmi1 = new Var<Boolean>(true);
	public static Var<Boolean> FLAGDEFAULT_enable_bmi1 = new Var<Boolean>(true); 
	
	public static Var<Boolean> FLAG_enable_bmi2 = new Var<Boolean>(true);
	public static Var<Boolean> FLAGDEFAULT_enable_bmi2 = new Var<Boolean>(true); 
	
	public static Var<Boolean> FLAG_enable_lzcnt = new Var<Boolean>(true);
	public static Var<Boolean> FLAGDEFAULT_enable_lzcnt = new Var<Boolean>(true); 
	
	public static Var<Boolean> FLAG_enable_popcnt = new Var<Boolean>(true);
	public static Var<Boolean> FLAGDEFAULT_enable_popcnt = new Var<Boolean>(true); 
	
	public static Var<String> FLAG_mcpu = new Var<String>("auto");
	public static Var<String> FLAGDEFAULT_mcpu = new Var<String>("auto");
	
	public static Var<String> FLAG_expose_gc_as = new Var<String>(null);
	public static Var<String> FLAGDEFAULT_expose_gc_as = new Var<String>(null);
              
    public static Var<String> FLAG_expose_cputracemark_as = new Var<String>(null);
	public static Var<String> FLAGDEFAULT_expose_cputracemark_as = new Var<String>(null);
	
	public static Var<Boolean> FLAG_wasm_shared_engine = new Var<Boolean>(true);
	public static Var<Boolean> FLAGDEFAULT_wasm_shared_engine = new Var<Boolean>(true); 
	
	public static Var<Integer> FLAG_wasm_max_code_space = new Var<Integer>(kMaxWasmCodeMB);
	public static Var<Integer> FLAGDEFAULT_wasm_max_code_space = new Var<Integer>(kMaxWasmCodeMB); 
	
	public static Var<Boolean> FLAG_trace_zone_stats = new Var<Boolean>(false);
	public static Var<Boolean> FLAGDEFAULT_trace_zone_stats = new Var<Boolean>(false); 
	
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
			new Flag(Flag.FlagType.TYPE_STRING, "gc_fake_mmap", FLAG_gc_fake_mmap, FLAGDEFAULT_gc_fake_mmap, "Specify the name of the file for fake gc mmap used in ll_prof", false),
			new Flag(Flag.FlagType.TYPE_BOOL, "enable_sse4_2", FLAG_enable_sse4_2, FLAGDEFAULT_enable_sse4_2, "enable use of SSE4.2 instructions if available", false),
			new Flag(Flag.FlagType.TYPE_BOOL, "enable_sse4_1", FLAG_enable_sse4_1, FLAGDEFAULT_enable_sse4_1, "enable use of SSE4.1 instructions if available", false),
			new Flag(Flag.FlagType.TYPE_BOOL, "enable_sse3", FLAG_enable_sse3, FLAGDEFAULT_enable_sse3, "enable use of SSE3 instructions if available", false),
			new Flag(Flag.FlagType.TYPE_BOOL, "enable_ssse3", FLAG_enable_ssse3, FLAGDEFAULT_enable_ssse3, "enable use of SSSE3 instructions if available", false),
			new Flag(Flag.FlagType.TYPE_BOOL, "enable_sahf", FLAG_enable_sahf, FLAGDEFAULT_enable_sahf, "enable use of SAHF instruction if available (X64 only)", false),
			new Flag(Flag.FlagType.TYPE_BOOL, "enable_avx", FLAG_enable_avx, FLAGDEFAULT_enable_avx, "enable use of AVX instructions if available", false),
			new Flag(Flag.FlagType.TYPE_BOOL, "enable_fma3", FLAG_enable_fma3, FLAGDEFAULT_enable_fma3, "enable use of FMA3 instructions if available", false),
			new Flag(Flag.FlagType.TYPE_BOOL, "enable_bmi1", FLAG_enable_bmi1, FLAGDEFAULT_enable_bmi1, "enable use of BMI1 instructions if available", false),
			new Flag(Flag.FlagType.TYPE_BOOL, "enable_bmi2", FLAG_enable_bmi2, FLAGDEFAULT_enable_bmi2, "enable use of BMI2 instructions if available", false),
			new Flag(Flag.FlagType.TYPE_BOOL, "enable_lzcnt", FLAG_enable_lzcnt, FLAGDEFAULT_enable_lzcnt, "enable use of LZCNT instruction if available", false),
			new Flag(Flag.FlagType.TYPE_BOOL, "enable_popcnt", FLAG_enable_popcnt, FLAGDEFAULT_enable_popcnt, "enable use of POPCNT instruction if available", false),
			new Flag(Flag.FlagType.TYPE_STRING, "mcpu", FLAG_mcpu, FLAGDEFAULT_mcpu, "enable optimization for specific cpu", false),
			new Flag(Flag.FlagType.TYPE_STRING, "expose_gc_as", FLAG_expose_gc_as, FLAGDEFAULT_expose_gc_as, "expose gc extension under the specified name", false),
			new Flag(Flag.FlagType.TYPE_STRING, "expose_cputracemark_as", FLAG_expose_cputracemark_as, FLAGDEFAULT_expose_cputracemark_as, "expose cputracemark extension under the specified name", false),
			new Flag(Flag.FlagType.TYPE_BOOL, "wasm_shared_engine", FLAG_wasm_shared_engine, FLAGDEFAULT_wasm_shared_engine, "shares one wasm engine between all isolates within a process", false),
			new Flag(Flag.FlagType.TYPE_UINT, "wasm_max_code_space", FLAG_wasm_max_code_space, FLAGDEFAULT_wasm_max_code_space, "maximum committed code space for wasm (in MB)", false),
			new Flag(Flag.FlagType.TYPE_BOOL, "trace_zone_stats", FLAG_trace_zone_stats, FLAGDEFAULT_trace_zone_stats, "trace zone memory usage", false)
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
	
	public static int xgetbv(int xcr) {
		return 0x3;
	}
	
	public static boolean OSHasAVXSupport() {
		int feature_mask = xgetbv(0);
		return (feature_mask & 0x6) == 0x6;
	}
	
	public static String GCFunctionName() {
		boolean flag_given = FLAG_expose_gc_as.getValue() != null && FLAG_expose_gc_as.getValue().length() != 0;
		return flag_given ? FLAG_expose_gc_as.getValue() : "gc";
	}
	
	public static int sNPrintF(Vector<Character> str, String format, Object... args) {
		int result = vSNPrintF(str, format, args);
		return result;
	}
	
	public static int vSNPrintF(Vector<Character> str, String format, Object... args) {
		return OS.vSNPrintF((char[]) str.begin(), str.length(), format, args);
	}
	
	public static boolean isValidCpuTraceMarkFunctionName() {
		return FLAG_expose_cputracemark_as.getValue() != null && FLAG_expose_cputracemark_as.getValue().length() != 0;
	}
	
	public static Register rax = Register.from_code(RegisterCode.kRegCode_rax.ordinal());
	public static Register rcx = Register.from_code(RegisterCode.kRegCode_rcx.ordinal());
	public static Register rdx = Register.from_code(RegisterCode.kRegCode_rdx.ordinal());
	public static Register rbx = Register.from_code(RegisterCode.kRegCode_rbx.ordinal());
	public static Register rsp = Register.from_code(RegisterCode.kRegCode_rsp.ordinal());
	public static Register rbp = Register.from_code(RegisterCode.kRegCode_rbp.ordinal());
	public static Register rsi = Register.from_code(RegisterCode.kRegCode_rsi.ordinal());
	public static Register rdi = Register.from_code(RegisterCode.kRegCode_rdi.ordinal());
	public static Register r8 = Register.from_code(RegisterCode.kRegCode_r8.ordinal());
	public static Register r9 = Register.from_code(RegisterCode.kRegCode_r9.ordinal());
	public static Register r10 = Register.from_code(RegisterCode.kRegCode_r10.ordinal());
	public static Register r11 = Register.from_code(RegisterCode.kRegCode_r11.ordinal());
	public static Register r12 = Register.from_code(RegisterCode.kRegCode_r12.ordinal());
	public static Register r13 = Register.from_code(RegisterCode.kRegCode_r13.ordinal());
	public static Register r14 = Register.from_code(RegisterCode.kRegCode_r14.ordinal());
	public static Register r15 = Register.from_code(RegisterCode.kRegCode_r15.ordinal());
	
	public static Register no_reg = Register.no_reg();
	
	public static Register kReturnRegister0 = rax;
	public static Register kReturnRegister1 = rdx;
	public static Register kReturnRegister2 = r8;
	public static Register kJSFunctionRegister = rdi;
	public static Register kContextRegister = rsi;
	public static Register kAllocateSizeRegister = rdx;
	public static Register kSpeculationPoisonRegister = r12;
	public static Register kInterpreterAccumulatorRegister = rax;
	public static Register kInterpreterBytecodeOffsetRegister = r9;
	public static Register kInterpreterBytecodeArrayRegister = r14;
	public static Register kInterpreterDispatchTableRegister = r15;

	public static Register kJavaScriptCallArgCountRegister = rax;
	public static Register kJavaScriptCallCodeStartRegister = rcx;
	public static Register kJavaScriptCallTargetRegister = kJSFunctionRegister;
	public static Register kJavaScriptCallNewTargetRegister = rdx;
	public static Register kJavaScriptCallExtraArg1Register = rbx;
	
	public static Register arg_reg_1 = rcx;
	public static Register arg_reg_2 = rdx;
	public static Register arg_reg_3 = r8;
	public static Register arg_reg_4 = r9;
	
	public static Register kRuntimeCallFunctionRegister = rbx;
	public static Register kRuntimeCallArgCountRegister = rax;
	public static Register kRuntimeCallArgvRegister = r15;
	public static Register kWasmInstanceRegister = rsi;
	
	// this method does not create an array, it is existed only to align with the v8 c++ codes
	public static <T> T[] newArray(T[] array) {
		return array;
	}
	
	public static <T> T[] newArray(T[] array, T default_val) {
		for(int index = 0, limit = array.length; index < limit; index++) {
			array[index] = default_val;
		}
		return array;
	}
	
	public static void fatalProcessOutOfMemory(org.jsengine.v8.internal.Isolate isolate, String location) {
		org.jsengine.v8.internal.V8.fatalProcessOutOfMemory(isolate, location, false);
	}
	
	public static int kMaxBuiltinRegisterParams = 5;
	public static int kMaxTFSBuiltinRegisterParams = kMaxBuiltinRegisterParams;
	
	public static void interpreterCEntryDescriptor_InitializePlatformSpecific(CallInterfaceDescriptorData data) {
		Register registers[] = { Internal.kRuntimeCallArgCountRegister, Internal.kRuntimeCallArgvRegister, Internal.kRuntimeCallFunctionRegister};
		data.initializePlatformSpecific(registers.length, registers);
	}
	
	public static int kAllocationTries = 2;
	
	public static Pointer<Object> allocatePages(PageAllocator page_allocator, Pointer<Object> hint, long size, long alignment, PageAllocator.Permission access) {
		Pointer<Object> result = Globals.nullptr;
		for (int i = 0; i < kAllocationTries; ++i) {
			result = page_allocator.allocatePages(hint, size, alignment, access);
			System.out.println("Internal::result: " + result.getValue());
			if (result != null) break;
			long request_size = size + alignment - page_allocator.allocatePageSize();
			//if (!OnCriticalMemoryPressure(request_size)) break;
		}
		return result;
	}
	
	public static boolean freePages(PageAllocator page_allocator, Pointer<Object> address, long size) {
		return page_allocator.freePages(address, size);
	}
	
	public static long kPtrComprHeapReservationSize = 4L * GB;
	public static long kPtrComprIsolateRootBias = kPtrComprHeapReservationSize / 2;
	public static long kPtrComprIsolateRootAlignment = 4L * GB;
	
	public static boolean setPermissions(PageAllocator page_allocator, Pointer<Object> address, long size, PageAllocator.Permission access) {
		return page_allocator.setPermissions(address, size, access);
	}
	
	public static AtomicInteger isolate_counter = new AtomicInteger(0);
	
	public static boolean V8_SFI_HAS_UNIQUE_ID = false;
	public static boolean TAGGED_SIZE_8_BYTES = false;
	public static boolean V8_DOUBLE_FIELDS_UNBOXING = false;
}