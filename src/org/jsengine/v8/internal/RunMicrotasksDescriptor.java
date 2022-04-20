package org.jsengine.v8.internal;

import org.jsengine.v8.Internal;

public class RunMicrotasksDescriptor extends CallInterfaceDescriptor {
	public static int kDescriptorFlags = CallInterfaceDescriptorData.Flag.kNoFlags.ordinal();
	public static int kReturnCount = 1;
	
	public static enum ParameterIndices {
		__dummy(-1), 
		kMicrotaskQueue(0),

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
		MachineType machine_types[] = { MachineType.anyTagged(), MachineType.pointer() };
    	data.initializePlatformIndependent(new CallInterfaceDescriptorData.Flags(kDescriptorFlags), kReturnCount, ParameterIndices.kParameterCount.value(), machine_types, machine_types.length);
	}
	
	public RunMicrotasksDescriptor() {
		super(key());
	}
	
	public static CallDescriptors.Key key() {
		return CallDescriptors.Key.RunMicrotasks;
	}
	
	protected void initializePlatformSpecific(CallInterfaceDescriptorData data) {
		defaultInitializePlatformSpecific(data, ParameterIndices.kParameterCount.value());
	}
	
	protected RunMicrotasksDescriptor(CallDescriptors.Key key) {
		super(key);
	}

	public static Register microtaskQueueRegister() {
		return CallDescriptors.call_descriptor_data(CallDescriptors.Key.RunMicrotasks).register_param(0);
	}
};