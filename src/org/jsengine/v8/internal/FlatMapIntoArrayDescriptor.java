package org.jsengine.v8.internal;

import org.jsengine.v8.Internal;

public class FlatMapIntoArrayDescriptor extends CallInterfaceDescriptor {
	public static int kDescriptorFlags = CallInterfaceDescriptorData.Flag.kNoFlags.ordinal();
	public static int kReturnCount = 1;
	
	public static enum ParameterIndices {
		__dummy(-1), 
		kTarget(0),
		kSource(1),
		kSourceLength(2),
		kStart(3),
		kDepth(4),
		kMapperFunction(5),
		kThisArg(6),

		kParameterCount(7),
		kContext(7); // = kParameterCount

		int value_;
		ParameterIndices(int value) {
			value_ = value;
		}
		
		public int value() {
			return value_;
		}
	};
	
	public FlatMapIntoArrayDescriptor() {
		super(key()); 
	}
	
	public static CallDescriptors.Key key() {
		return CallDescriptors.Key.FlatMapIntoArray;
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

	protected FlatMapIntoArrayDescriptor(CallDescriptors.Key key) {
		super(key);
	}
};