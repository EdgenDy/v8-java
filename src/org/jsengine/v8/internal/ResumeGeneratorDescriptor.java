package org.jsengine.v8.internal;

import org.jsengine.v8.Internal;

public class ResumeGeneratorDescriptor extends CallInterfaceDescriptor {
	public static int kDescriptorFlags = CallInterfaceDescriptorData.Flag.kNoFlags.ordinal();
	public static int kReturnCount = 1;
	
	public static enum ParameterIndices {
		__dummy(-1), 
		kValue(0),
		kGenerator(1),

		kParameterCount(2),
		kContext(2); // = kParameterCount

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
		MachineType machine_types[] = { MachineType.anyTagged(), MachineType.anyTagged(), MachineType.anyTagged() };
    	data.initializePlatformIndependent(new CallInterfaceDescriptorData.Flags(kDescriptorFlags), kReturnCount, ParameterIndices.kParameterCount.value(), machine_types, machine_types.length);
	}
	
	public ResumeGeneratorDescriptor() {
		super(key());
	}
	
	public static CallDescriptors.Key key() {
		return CallDescriptors.Key.ResumeGenerator;
	}
	
	protected void initializePlatformSpecific(CallInterfaceDescriptorData data) {
		
	}
	
	protected ResumeGeneratorDescriptor(CallDescriptors.Key key) {
		super(key);
	}
};