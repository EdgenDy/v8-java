package org.jsengine.v8.internal;

import org.jsengine.v8.Internal;

public class DecrementDescriptor extends CallInterfaceDescriptor {
	public static int kDescriptorFlags = CallInterfaceDescriptorData.Flag.kNoFlags.ordinal();
	public static int kReturnCount = 1;
	
	public static enum ParameterIndices {
		__dummy(-1), 
		kValue(0),

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
	
	public DecrementDescriptor() {
		super(key()); 
	}
	
	public static CallDescriptors.Key key() {
		return CallDescriptors.Key.Decrement;
	}
    
	protected static int kRegisterParams = 
      ParameterIndices.kParameterCount.value() > Internal.kMaxTFSBuiltinRegisterParams
          ? Internal.kMaxTFSBuiltinRegisterParams 
          : ParameterIndices.kParameterCount.value(); 
	protected static int kStackParams = ParameterIndices.kParameterCount.value() - kRegisterParams;
	
	@Override
	protected void initializePlatformSpecific(CallInterfaceDescriptorData data) {
		defaultInitializePlatformSpecific(data, kRegisterParams);
	}
	
	@Override
	protected void initializePlatformIndependent(CallInterfaceDescriptorData data) {
		data.initializePlatformIndependent(new CallInterfaceDescriptorData.Flags(kDescriptorFlags), kReturnCount, ParameterIndices.kParameterCount.value(), null, 0);
	}

	protected DecrementDescriptor(CallDescriptors.Key key) {
		super(key);
	}
};