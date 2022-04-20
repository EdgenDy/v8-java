package org.jsengine.v8.internal;

import org.jsengine.v8.Internal;

public class InterpreterDispatchDescriptor extends CallInterfaceDescriptor {
	public static int kDescriptorFlags = CallInterfaceDescriptorData.Flag.kNoFlags.ordinal();
	public static int kReturnCount = 1;
	
	public static enum ParameterIndices {
		__dummy(-1), 
		kAccumulator(0),
		kBytecodeOffset(1),
		kBytecodeArray(2),
		kDispatchTable(3),

		kParameterCount(4),
		kContext(4); // = kParameterCount

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
		MachineType machine_types[] = { MachineType.anyTagged(), MachineType.anyTagged(), MachineType.intPtr(), MachineType.anyTagged(), MachineType.intPtr() };
    	data.initializePlatformIndependent(new CallInterfaceDescriptorData.Flags(kDescriptorFlags), kReturnCount, ParameterIndices.kParameterCount.value(), machine_types, machine_types.length);
	}
	
	public InterpreterDispatchDescriptor() {
		super(key());
	}
	
	public static CallDescriptors.Key key() {
		return CallDescriptors.Key.InterpreterDispatch;
	}
	
	protected void initializePlatformSpecific(CallInterfaceDescriptorData data) {
		Register registers[] = { Internal.kInterpreterAccumulatorRegister, Internal.kInterpreterBytecodeOffsetRegister, Internal.kInterpreterBytecodeArrayRegister, Internal.kInterpreterDispatchTableRegister };
		data.initializePlatformSpecific(registers.length, registers);
	}
	
	protected InterpreterDispatchDescriptor(CallDescriptors.Key key) {
		super(key);
	}
};