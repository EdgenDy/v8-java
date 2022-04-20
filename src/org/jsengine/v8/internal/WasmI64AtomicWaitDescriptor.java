package org.jsengine.v8.internal;

import org.jsengine.v8.Internal;

public class WasmI64AtomicWaitDescriptor extends CallInterfaceDescriptor {
	public static int kDescriptorFlags = CallInterfaceDescriptorData.Flag.kNoContext.value();
	public static int kReturnCount = 1;
	
	public static enum ParameterIndices {
		__dummy(-1), 
		kAddress(0),
		kExpectedValueHigh(1),
		kExpectedValueLow(2),
		kTimeout(3),

		kParameterCount(4);
		
		int value_;
		ParameterIndices(int value) {
			value_ = value;
		}
		
		public int value() {
			return value_;
		}
	};
	
	@Override
	public void initializePlatformIndependent(CallInterfaceDescriptorData data) {
		MachineType machine_types[] = { MachineType.uint32(), MachineType.uint32(), MachineType.uint32(), MachineType.uint32(), MachineType.float64(), };
		data.initializePlatformIndependent(new CallInterfaceDescriptorData.Flags(kDescriptorFlags), kReturnCount, ParameterIndices.kParameterCount.value(), machine_types, machine_types.length);
	}
	
	public WasmI64AtomicWaitDescriptor() {
		super(key());
	}
	
	public static CallDescriptors.Key key() {
		return CallDescriptors.Key.WasmI64AtomicWait;
	}
	
	protected void initializePlatformSpecific(CallInterfaceDescriptorData data) {
		
	}
	
	protected WasmI64AtomicWaitDescriptor(CallDescriptors.Key key) {
		super(key);
	}
};