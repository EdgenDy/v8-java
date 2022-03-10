package org.jsengine.v8.internal;

public enum CpuFeature {
	SSE4_2(0),	
	SSE4_1(1),
	SSSE3(2),
	SSE3(3),
	SAHF(4),
	AVX(5),
	FMA3(6),
	BMI1(7),
	BMI2(8),
	LZCNT(9),
	POPCNT(10),
	ATOM(11),
	ARMv7(12),
	ARMv7_SUDIV(13),
	ARMv8(14),
	FPU(15),
	FP64FPU(16),
	MIPSr1(17),
	MIPSr2(18),
	MIPSr6(19),
	MIPS_SIMD(20),
	FPR_GPR_MOV(21),
	LWSYNC(22),
	ISELECT(23),
	VSX(24),
	MODULO(25),
	DISTINCT_OPS(26),
	GENERAL_INSTR_EXT(27),
	FLOATING_POINT_EXT(28),
	VECTOR_FACILITY(29),
	VECTOR_ENHANCE_FACILITY_1(30),
	MISC_INSTR_EXT2(31),	
	NUMBER_OF_CPU_FEATURES(32),
	VFPv3(12),// = ARMv7(),,
	NEON(12),// = ARMv7(),
	VFP32DREGS(12),// = ARMv7(),
	SUDIV(13);// = ARMv7_SUDIV;
	
	int value_;
	CpuFeature(int value) {
		value_ = value;
	}
	
	public int value() {
		return value_;
	}
}