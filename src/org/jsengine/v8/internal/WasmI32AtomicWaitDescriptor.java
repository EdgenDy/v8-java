package org.jsengine.v8.internal;

import org.jsengine.v8.Internal;

class WasmI32AtomicWaitDescriptor extends CallInterfaceDescriptor {
	public static int kDescriptorFlags = CallInterfaceDescriptorData.Flag.kNoContext.value();
	public static int kReturnCount = 1;
	
	public static enum ParameterIndices {
		__dummy(-1), 
		kAddress(0),
		kExpectedValue(1),
		kTimeout(2),

		kParameterCount(3);
		
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
		MachineType machine_types[] = { MachineType.uint32(), MachineType.uint32(), MachineType.int32(), MachineType.float64() };
		data.initializePlatformIndependent(new CallInterfaceDescriptorData.Flags(kDescriptorFlags), kReturnCount, ParameterIndices.kParameterCount.value(), machine_types, machine_types.length);
	}
	
	public WasmI32AtomicWaitDescriptor() {
		super(key());
	}
	
	public static CallDescriptors.Key key() {
		return CallDescriptors.Key.WasmI32AtomicWait;
	}
	
	protected void initializePlatformSpecific(CallInterfaceDescriptorData data) {
		
	}
	
	protected WasmI32AtomicWaitDescriptor(CallDescriptors.Key key) {
		super(key);
	}
};