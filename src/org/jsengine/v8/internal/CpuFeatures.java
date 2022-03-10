package org.jsengine.v8.internal;

import org.jsengine.v8.Internal;
import org.jsengine.v8.base.CPU;

// src\codegen\cpu-features.h:73
public class CpuFeatures {
	public static int supported_;
	public static boolean initialized_;
	
	
	// src\codegen\cpu-features.h:75
	public static void probe(boolean cross_compile) {
		if (initialized_) return;
		initialized_ = true;
		probeImpl(cross_compile);
	}
	
	// src\codegen\cpu-features.h:115
	private static void probeImpl(boolean cross_compile) {
		CPU cpu = new CPU();
		if (cross_compile) return;
		
		if (cpu.has_sse42() && Internal.FLAG_enable_sse4_2.getValue()) supported_ |= 1 << CpuFeature.SSE4_2.value();
		if (cpu.has_sse41() && Internal.FLAG_enable_sse4_1.getValue()) {
			supported_ |= 1 << CpuFeature.SSE4_1.value();
			supported_ |= 1 << CpuFeature.SSSE3.value();
		}
		
		if (cpu.has_ssse3() && Internal.FLAG_enable_ssse3.getValue()) supported_ |= 1 << CpuFeature.SSSE3.value();
		if (cpu.has_sse3() && Internal.FLAG_enable_sse3.getValue()) supported_ |= 1 << CpuFeature.SSE3.value();
		
		if (cpu.has_sahf() && Internal.FLAG_enable_sahf.getValue()) supported_ |= 1 << CpuFeature.SAHF.value();
		
		if (cpu.has_avx() && Internal.FLAG_enable_avx.getValue() && cpu.has_osxsave() && Internal.OSHasAVXSupport()) {
			supported_ |= 1 << CpuFeature.AVX.value();
		}
		
		if (cpu.has_fma3() && Internal.FLAG_enable_fma3.getValue() && cpu.has_osxsave() && Internal.OSHasAVXSupport()) {
			supported_ |= 1 << CpuFeature.FMA3.value();
		}
		
		if (cpu.has_bmi1() && Internal.FLAG_enable_bmi1.getValue()) supported_ |= 1 << CpuFeature.BMI1.value();
		if (cpu.has_bmi2() && Internal.FLAG_enable_bmi2.getValue()) supported_ |= 1 << CpuFeature.BMI2.value();
		if (cpu.has_lzcnt() && Internal.FLAG_enable_lzcnt.getValue()) supported_ |= 1 << CpuFeature.LZCNT.value();
		if (cpu.has_popcnt() && Internal.FLAG_enable_popcnt.getValue()) supported_ |= 1 << CpuFeature.POPCNT.value();
		
		if (Internal.FLAG_mcpu.getValue() == "auto") {
			if (cpu.is_atom()) supported_ |= 1 << CpuFeature.ATOM.value();
		} 
		else if (Internal.FLAG_mcpu.getValue() == "atom") {
			supported_ |= 1 << CpuFeature.ATOM.value();
		}
	}
}