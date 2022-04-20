package org.jsengine.v8.internal;

import org.jsengine.v8.Internal;

public class JSTrampolineDescriptor extends CallInterfaceDescriptor {
	public static int kDescriptorFlags = CallInterfaceDescriptorData.Flag.kNoFlags.ordinal();
	public static int kReturnCount = 1;

	public static enum ParameterIndices {
		kTarget(0),
		kNewTarget(1),
		kActualArgumentsCount(2),
		
		kParameterCount(3),
		kContext(3); //,= kParameterCount ;
		
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
		MachineType machine_types[] = { MachineType.anyTagged(), MachineType.anyTagged(), MachineType.anyTagged(), MachineType.int32() };
    	data.initializePlatformIndependent(new CallInterfaceDescriptorData.Flags(kDescriptorFlags), kReturnCount, ParameterIndices.kParameterCount.value(), machine_types, machine_types.length);
	}
	
	public JSTrampolineDescriptor() {
		super(key());
	}
	
	public static CallDescriptors.Key key() {
		return CallDescriptors.Key.JSTrampoline;
	}
	
	@Override
	protected void initializePlatformSpecific(CallInterfaceDescriptorData data) {
		jSDefaultInitializePlatformSpecific(data, 0);
	}
	
	protected JSTrampolineDescriptor(CallDescriptors.Key key) {
		super(key);
	}
};