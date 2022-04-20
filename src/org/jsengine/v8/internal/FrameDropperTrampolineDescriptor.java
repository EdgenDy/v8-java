package org.jsengine.v8.internal;

import org.jsengine.v8.Internal;

public class FrameDropperTrampolineDescriptor extends CallInterfaceDescriptor {
	public static int kDescriptorFlags = CallInterfaceDescriptorData.Flag.kNoFlags.ordinal();
	public static int kReturnCount = 1;
	
	public static enum ParameterIndices {
		__dummy(-1), 
		kRestartFp(0),

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
	
	public FrameDropperTrampolineDescriptor() {
		super(key());
	}
	
	public static CallDescriptors.Key key() {
		return CallDescriptors.Key.FrameDropperTrampoline;
	}
	
	protected void initializePlatformSpecific(CallInterfaceDescriptorData data) {
		Register registers[] = { Internal.rbx };
		data.initializePlatformSpecific(registers.length, registers);
	}
	
	protected FrameDropperTrampolineDescriptor(CallDescriptors.Key key) {
		super(key);
	}
};