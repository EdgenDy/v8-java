package org.jsengine.v8.base;

import org.jsengine.Globals;

// src/base/cpu.h:32
public final class CPU {
	char vendor_[] = new char[13];
	private int stepping_;
	private int model_;
	private int ext_model_;
	private int family_;
	private int ext_family_;
	private int type_;
	private int implementer_;
	private int architecture_;
	private int variant_;
	private int part_;
	private int icache_line_size_;
	private int dcache_line_size_;
	private boolean has_fpu_;
	private boolean has_cmov_;
	private boolean has_sahf_;
	private boolean has_mmx_;
	private boolean has_sse_;
	private boolean has_sse2_;
	private boolean has_sse3_;
	private boolean has_ssse3_;
	private boolean has_sse41_;
	private boolean has_sse42_;
	private boolean is_atom_;
	private boolean has_osxsave_;
	private boolean has_avx_;
	private boolean has_fma3_;
	private boolean has_bmi1_;
	private boolean has_bmi2_;
	private boolean has_lzcnt_;
	private boolean has_popcnt_;
	private boolean has_idiva_;
	private boolean has_neon_;
	private boolean has_thumb2_;
	private boolean has_vfp_;
	private boolean has_vfp3_;
	private boolean has_vfp3_d32_;
	private boolean is_fp64_mode_;
	private boolean has_non_stop_time_stamp_counter_;
	private boolean has_msa_;
	
	public char[] vendor() { return vendor_; }
	public int stepping() { return stepping_; }
	public int model() { return model_; }
	public int ext_model() { return ext_model_; }
	public int family() { return family_; }
	public int ext_family() { return ext_family_; }
	public int type() { return type_; }

	public int implementer() { return implementer_; }
	public static int ARM = 0x41;
	public static int NVIDIA = 0x4e;
	public static int QUALCOMM = 0x51;
	public int architecture() { return architecture_; }
	public int variant() { return variant_; }
	public static int NVIDIA_DENVER = 0x0;
	public int part() { return part_; }

	public static int ARM_CORTEX_A5 = 0xc05;
	public static int ARM_CORTEX_A7 = 0xc07;
	public static int ARM_CORTEX_A8 = 0xc08;
	public static int ARM_CORTEX_A9 = 0xc09;
	public static int ARM_CORTEX_A12 = 0xc0c;
	public static int ARM_CORTEX_A15 = 0xc0f;

	public static int NVIDIA_DENVER_V10 = 0x002;

	public enum _ {
		PPC_POWER5,
		PPC_POWER6,
		PPC_POWER7,
		PPC_POWER8,
		PPC_POWER9,
		PPC_G4,
		PPC_G5,
		PPC_PA6T
	};

	public boolean has_fpu() { return has_fpu_; }
	public int icache_line_size() { return icache_line_size_; }
	public int dcache_line_size() { return dcache_line_size_; }
	public static int UNKNOWN_CACHE_LINE_SIZE = 0;

	public boolean has_cmov() { return has_cmov_; }
	public boolean has_sahf() { return has_sahf_; }
	public boolean has_mmx() { return has_mmx_; }
	public boolean has_sse() { return has_sse_; }
	public boolean has_sse2() { return has_sse2_; }
	public boolean has_sse3() { return has_sse3_; }
	public boolean has_ssse3() { return has_ssse3_; }
	public boolean has_sse41() { return has_sse41_; }
	public boolean has_sse42() { return has_sse42_; }
	public boolean has_osxsave() { return has_osxsave_; }
	public boolean has_avx() { return has_avx_; }
	public boolean has_fma3() { return has_fma3_; }
	public boolean has_bmi1() { return has_bmi1_; }
	public boolean has_bmi2() { return has_bmi2_; }
	public boolean has_lzcnt() { return has_lzcnt_; }
	public boolean has_popcnt() { return has_popcnt_; }
	public boolean is_atom() { return is_atom_; }
	public boolean has_non_stop_time_stamp_counter() {
		return has_non_stop_time_stamp_counter_;
	}

	public boolean has_idiva() { return has_idiva_; }
	public boolean has_neon() { return has_neon_; }
	public boolean has_thumb2() { return has_thumb2_; }
	public boolean has_vfp() { return has_vfp_; }
	public boolean has_vfp3() { return has_vfp3_; }
	public boolean has_vfp3_d32() { return has_vfp3_d32_; }

	public boolean is_fp64_mode() { return is_fp64_mode_; }
	public boolean has_msa() { return has_msa_; }
	
// src/base/cpu.cc:300
	public CPU() {
		stepping_ = 0;
		model_ = 0;
		ext_model_= 0;
		family_ = 0;
		ext_family_ = 0;
		type_ = 0;
		implementer_ = 0;
		architecture_ = 0;
		variant_ = -1;
		part_ = 0;
		icache_line_size_ = (UNKNOWN_CACHE_LINE_SIZE);
		dcache_line_size_ = (UNKNOWN_CACHE_LINE_SIZE);
		has_fpu_ = false;
		has_cmov_ = false;
		has_sahf_ = false;
		has_mmx_ = false;
		has_sse_ = false;
		has_sse2_ = false;
		has_sse3_ = false;
		has_ssse3_ = false;
		has_sse41_ = false;
		has_sse42_ = false;
		is_atom_ = false;
		has_osxsave_ = false;
		has_avx_ = false;
		has_fma3_ = false;
		has_bmi1_ = false;
		has_bmi2_ = false;
		has_lzcnt_ = false;
		has_popcnt_ = false;
		has_idiva_ = false;
		has_neon_ = false;
		has_thumb2_ = false;
		has_vfp_ = false;
		has_vfp3_ = false;
		has_vfp3_d32_ = false;
		is_fp64_mode_ = false;
		has_non_stop_time_stamp_counter_ = false;
		has_msa_ = false;
		
		Globals.memcpy(vendor_, "Unknown", 7); // length is 7 character \0 is not included

		int cpu_info[] = new int[4];
		Globals.__cpuid(cpu_info, 0);
		int num_ids = cpu_info[0];
		
		Globals.Std.swap(cpu_info, cpu_info, 2, 3);
		Globals.memcpy(vendor_, cpu_info[1], cpu_info[2], cpu_info[3]);
		vendor_[12] = '\0';
		
		if (num_ids > 0) {
			Globals.__cpuid(cpu_info, 1);
			stepping_ = cpu_info[0] & 0xF;
			model_ = ((cpu_info[0] >> 4) & 0xF) + ((cpu_info[0] >> 12) & 0xF0);
			family_ = (cpu_info[0] >> 8) & 0xF;
			type_ = (cpu_info[0] >> 12) & 0x3;
			ext_model_ = (cpu_info[0] >> 16) & 0xF;
			ext_family_ = (cpu_info[0] >> 20) & 0xFF;
			has_fpu_ = (cpu_info[3] & 0x00000001) != 0;
			has_cmov_ = (cpu_info[3] & 0x00008000) != 0;
			has_mmx_ = (cpu_info[3] & 0x00800000) != 0;
			has_sse_ = (cpu_info[3] & 0x02000000) != 0;
			has_sse2_ = (cpu_info[3] & 0x04000000) != 0;
			has_sse3_ = (cpu_info[2] & 0x00000001) != 0;
			has_ssse3_ = (cpu_info[2] & 0x00000200) != 0;
			has_sse41_ = (cpu_info[2] & 0x00080000) != 0;
			has_sse42_ = (cpu_info[2] & 0x00100000) != 0;
			has_popcnt_ = (cpu_info[2] & 0x00800000) != 0;
			has_osxsave_ = (cpu_info[2] & 0x08000000) != 0;
			has_avx_ = (cpu_info[2] & 0x10000000) != 0;
			has_fma3_ = (cpu_info[2] & 0x00001000) != 0;

			if (family_ == 0x6) {
				switch (model_) {
					case 0x1C:
					case 0x26:
					case 0x36:
					case 0x27:
					case 0x35:
					case 0x37:
					case 0x4A:
					case 0x4D:
					case 0x4C:
					case 0x6E:
						is_atom_ = true;
				}
			}
		}
		
		if (num_ids >= 7) {
			Globals.__cpuid(cpu_info, 7);
			has_bmi1_ = (cpu_info[1] & 0x00000008) != 0;
			has_bmi2_ = (cpu_info[1] & 0x00000100) != 0;
		}
		
		Globals.__cpuid(cpu_info, 0x80000000);
		int num_ext_ids = cpu_info[0];
		
		if (num_ext_ids > 0x80000000) {
			Globals.__cpuid(cpu_info, 0x80000001);
			has_lzcnt_ = (cpu_info[2] & 0x00000020) != 0;
			has_sahf_ = (cpu_info[2] & 0x00000001) != 0;
		}
		
		int parameter_containing_non_stop_time_stamp_counter = 0x80000007;
		if (num_ext_ids >= parameter_containing_non_stop_time_stamp_counter) {
			Globals.__cpuid(cpu_info, parameter_containing_non_stop_time_stamp_counter);
			has_non_stop_time_stamp_counter_ = (cpu_info[3] & (1 << 8)) != 0;
		}
	}
}