package org.jsengine.v8.internal;

import org.jsengine.v8.Internal;

public class WasmAtomicNotifyDescriptor extends CallInterfaceDescriptor {
	public static int kDescriptorFlags = CallInterfaceDescriptorData.Flag.kNoContext.value();
	public static int kReturnCount = 1;
	
	public static enum ParameterIndices {
		__dummy(-1), 
		kAddress(0),
		kCount(1),

		kParameterCount(2);
		
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
		MachineType machine_types[] = { MachineType.uint32(), MachineType.uint32(), MachineType.uint32() };
		data.initializePlatformIndependent(new CallInterfaceDescriptorData.Flags(kDescriptorFlags), kReturnCount, ParameterIndices.kParameterCount.value(), machine_types, machine_types.length);
	}
	
	public WasmAtomicNotifyDescriptor() {
		super(key());
	}
	
	public static CallDescriptors.Key key() {
		return CallDescriptors.Key.WasmAtomicNotify;
	}
	
	protected void initializePlatformSpecific(CallInterfaceDescriptorData data) {
		
	}
	
	protected WasmAtomicNotifyDescriptor(CallDescriptors.Key key) {
		super(key);
	}
};