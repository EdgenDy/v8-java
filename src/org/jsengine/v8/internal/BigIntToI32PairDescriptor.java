package org.jsengine.v8.internal;

import org.jsengine.v8.Internal;

public class BigIntToI32PairDescriptor extends CallInterfaceDescriptor {
	public static int kDescriptorFlags = CallInterfaceDescriptorData.Flag.kNoFlags.value();
	public static int kReturnCount = 2;

	public static enum ParameterIndices {
		__dummy(-1), 
		kArgument(0),
		
		kParameterCount(1),
		kContext(1); // = kParameterCount
		
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
		MachineType machine_types[] = { MachineType.uint32(), MachineType.uint32(), MachineType.anyTagged() };
		data.initializePlatformIndependent(new CallInterfaceDescriptorData.Flags(kDescriptorFlags), kReturnCount, ParameterIndices.kParameterCount.value(), machine_types, machine_types.length);
	}
	
	public BigIntToI32PairDescriptor() {
		super(key());
	}
	
	public static CallDescriptors.Key key() {
		return CallDescriptors.Key.BigIntToI32Pair;
	}
	
	protected void initializePlatformSpecific(CallInterfaceDescriptorData data) {
		defaultInitializePlatformSpecific(data, ParameterIndices.kParameterCount.value());
	}
	
	protected BigIntToI32PairDescriptor(CallDescriptors.Key key) {
		super(key);
	}
};