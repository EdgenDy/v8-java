package org.jsengine.v8.internal;

public class CallDescriptors {
	public static enum Key {
		Abort,
		Allocate,
		AllocateHeapNumber,
		ApiCallback,
		ApiGetter,
		ArgumentsAdaptor,
		ArrayConstructor,
		ArrayNArgumentsConstructor,
		ArrayNoArgumentConstructor,
		ArraySingleArgumentConstructor,
		AsyncFunctionStackParameter,
		BigIntToI64,
		BigIntToI32Pair,
		I64ToBigInt,
		I32PairToBigInt,
		BinaryOp,
		CallForwardVarargs,
		CallFunctionTemplate,
		CallTrampoline,
		CallVarargs,
		CallWithArrayLike,
		CallWithSpread,
		CEntry1ArgvOnStack,
		CloneObjectWithVector,
		Compare,
		ConstructForwardVarargs,
		ConstructStub,
		ConstructVarargs,
		ConstructWithArrayLike,
		ConstructWithSpread,
		ContextOnly,
		CppBuiltinAdaptor,
		EphemeronKeyBarrier,
		FastNewFunctionContext,
		FastNewObject,
		FrameDropperTrampoline,
		GetIteratorStackParameter,
		GetProperty,
		GrowArrayElements,
		InterpreterCEntry1,
		InterpreterCEntry2,
		InterpreterDispatch,
		InterpreterPushArgsThenCall,
		InterpreterPushArgsThenConstruct,
		JSTrampoline,
		Load,
		LoadGlobal,
		LoadGlobalWithVector,
		LoadWithVector,
		NewArgumentsElements,
		NoContext,
		RecordWrite,
		ResumeGenerator,
		RunMicrotasksEntry,
		RunMicrotasks,
		Store,
		StoreGlobal,
		StoreGlobalWithVector,
		StoreTransition,
		StoreWithVector,
		StringAt,
		StringAtAsString,
		StringSubstring,
		TypeConversion,
		TypeConversionStackParameter,
		Typeof,
		Void,
		WasmAtomicNotify,
		WasmI32AtomicWait,
		WasmI64AtomicWait,
		WasmMemoryGrow,
		WasmTableGet,
		WasmTableSet,
		WasmThrow,
		
		
		
		FastNewClosure,
		CreateRegExpLiteral,
		CreateEmptyArrayLiteral,
		CreateShallowArrayLiteral,
		CreateShallowObjectLiteral,
		StringIndexOf,
		OrderedHashTableHealIndex,
		CopyFastSmiOrObjectElements,
		EnqueueMicrotask,
		HasProperty,
		DeleteProperty,
		CopyDataProperties,
		SetDataProperties,
		ArrayIncludesSmiOrObject,
		ArrayIncludesPackedDoubles,
		ArrayIncludesHoleyDoubles,
		ArrayIndexOfSmiOrObject,
		ArrayIndexOfPackedDoubles,
		ArrayIndexOfHoleyDoubles,
		CloneFastJSArray,
		CloneFastJSArrayFillingHoles,
		ExtractFastJSArray,
		FlattenIntoArray,
		FlatMapIntoArray,
		AsyncFunctionEnter,
		AsyncFunctionReject,
		AsyncFunctionResolve,
		AsyncFunctionAwaitCaught,
		AsyncFunctionAwaitUncaught,
		CreateIterResultObject,
		CreateGeneratorObject,
		IterableToList,
		IterableToListWithSymbolLookup,
		IterableToListMayPreserveHoles,
		FindOrderedHashMapEntry,
		MapIteratorToList,
		ParseInt,
		BitwiseNot,
		Decrement,
		Increment,
		Negate,
		CreateObjectWithoutProperties,
		ObjectToString,
		ForInEnumerate,
		ForInFilter,
		FulfillPromise,
		RejectPromise,
		ResolvePromise,
		NewPromiseCapability,
		PerformPromiseThen,
		PromiseRejectReactionJob,
		PromiseFulfillReactionJob,
		PromiseResolveThenableJob,
		PromiseResolve,
		RegExpExecAtom,
		RegExpExecInternal,
		RegExpPrototypeExecSlow,
		RegExpSearchFast,
		RegExpSplit,
		SetOrSetIteratorToList,
		ThrowWasmTrapUnreachable,
		ThrowWasmTrapMemOutOfBounds,
		ThrowWasmTrapUnalignedAccess,
		ThrowWasmTrapDivByZero,
		ThrowWasmTrapDivUnrepresentable,
		ThrowWasmTrapRemByZero,
		ThrowWasmTrapFloatUnrepresentable,
		ThrowWasmTrapFuncInvalid,
		ThrowWasmTrapFuncSigMismatch,
		ThrowWasmTrapDataSegmentDropped,
		ThrowWasmTrapElemSegmentDropped,
		ThrowWasmTrapTableOutOfBounds,
		WeakMapLookupHashIndex,
		WeakCollectionDelete,
		WeakCollectionSet,
		AsyncGeneratorResolve,
		AsyncGeneratorReject,
		AsyncGeneratorYield,
		AsyncGeneratorReturn,
		AsyncGeneratorResumeNext,
		AsyncGeneratorAwaitCaught,
		AsyncGeneratorAwaitUncaught,
		StringAdd_CheckNone,
		SubString,
		GetPropertyWithReceiver,
		SetProperty,
		SetPropertyInLiteral,
		
		StringToLowerCaseIntl,
		
		NUMBER_OF_DESCRIPTORS,
	};
	
	private static CallInterfaceDescriptorData call_descriptor_data_[];
	
	static {
		call_descriptor_data_ = new CallInterfaceDescriptorData[Key.NUMBER_OF_DESCRIPTORS.ordinal()];
		
		for(int index = 0, limit = Key.NUMBER_OF_DESCRIPTORS.ordinal(); index < limit; index++) {
			call_descriptor_data_[index] = new CallInterfaceDescriptorData();
		}
	}
	
	public static CallInterfaceDescriptorData call_descriptor_data(CallDescriptors.Key key) {
    	return call_descriptor_data_[key.ordinal()];
	}
	
	public static void initializeOncePerProcess() {
		new AbortDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.Abort.ordinal()]);
		new AllocateDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.Allocate.ordinal()]);
		new AllocateHeapNumberDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.AllocateHeapNumber.ordinal()]);
		new ApiCallbackDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.ApiCallback.ordinal()]);
		new ApiGetterDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.ApiGetter.ordinal()]);
		new ArgumentsAdaptorDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.ArgumentsAdaptor.ordinal()]);
		new ArrayConstructorDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.ArrayConstructor.ordinal()]);
		new ArrayNArgumentsConstructorDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.ArrayNArgumentsConstructor.ordinal()]);
		new ArrayNoArgumentConstructorDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.ArrayNoArgumentConstructor.ordinal()]);
		new ArraySingleArgumentConstructorDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.ArraySingleArgumentConstructor.ordinal()]);
		new AsyncFunctionStackParameterDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.AsyncFunctionStackParameter.ordinal()]);
		new BigIntToI64Descriptor().initialize(call_descriptor_data_[CallDescriptors.Key.BigIntToI64.ordinal()]);
		new BigIntToI32PairDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.BigIntToI32Pair.ordinal()]);
		new I64ToBigIntDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.I64ToBigInt.ordinal()]);
		new I32PairToBigIntDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.I32PairToBigInt.ordinal()]);
		new BinaryOpDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.BinaryOp.ordinal()]);
		new CallForwardVarargsDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.CallForwardVarargs.ordinal()]);
		new CallFunctionTemplateDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.CallFunctionTemplate.ordinal()]);
		new CallTrampolineDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.CallTrampoline.ordinal()]);
		new CallVarargsDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.CallVarargs.ordinal()]);
		new CallWithArrayLikeDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.CallWithArrayLike.ordinal()]);
		new CallWithSpreadDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.CallWithSpread.ordinal()]);
		new CEntry1ArgvOnStackDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.CEntry1ArgvOnStack.ordinal()]);
		new CloneObjectWithVectorDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.CloneObjectWithVector.ordinal()]);
		new CompareDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.Compare.ordinal()]);
		new ConstructForwardVarargsDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.ConstructForwardVarargs.ordinal()]);
		new ConstructStubDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.ConstructStub.ordinal()]);
		new ConstructVarargsDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.ConstructVarargs.ordinal()]);
		new ConstructWithArrayLikeDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.ConstructWithArrayLike.ordinal()]);
		new ConstructWithSpreadDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.ConstructWithSpread.ordinal()]);
		new CppBuiltinAdaptorDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.CppBuiltinAdaptor.ordinal()]);
		new EphemeronKeyBarrierDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.EphemeronKeyBarrier.ordinal()]);
		new FastNewFunctionContextDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.FastNewFunctionContext.ordinal()]);
		new FastNewObjectDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.FastNewObject.ordinal()]);
		new FrameDropperTrampolineDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.FrameDropperTrampoline.ordinal()]);
		new GetIteratorStackParameterDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.GetIteratorStackParameter.ordinal()]);
		new GetPropertyDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.GetProperty.ordinal()]);
		new GrowArrayElementsDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.GrowArrayElements.ordinal()]);
		new InterpreterCEntry1Descriptor().initialize(call_descriptor_data_[CallDescriptors.Key.InterpreterCEntry1.ordinal()]);
		new InterpreterCEntry2Descriptor().initialize(call_descriptor_data_[CallDescriptors.Key.InterpreterCEntry2.ordinal()]);
		new InterpreterDispatchDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.InterpreterDispatch.ordinal()]);
		new InterpreterPushArgsThenCallDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.InterpreterPushArgsThenCall.ordinal()]);
		new InterpreterPushArgsThenConstructDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.InterpreterPushArgsThenConstruct.ordinal()]);
		new JSTrampolineDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.JSTrampoline.ordinal()]);
		new LoadDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.Load.ordinal()]);
		new LoadGlobalDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.LoadGlobal.ordinal()]);
		new LoadGlobalWithVectorDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.LoadGlobalWithVector.ordinal()]);
		new LoadWithVectorDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.LoadWithVector.ordinal()]);
		new NewArgumentsElementsDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.NewArgumentsElements.ordinal()]);
		new NoContextDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.NoContext.ordinal()]);
		new RecordWriteDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.RecordWrite.ordinal()]);
		new ResumeGeneratorDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.ResumeGenerator.ordinal()]);
		new RunMicrotasksEntryDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.RunMicrotasksEntry.ordinal()]);
		new RunMicrotasksDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.RunMicrotasks.ordinal()]);
		new StoreDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.Store.ordinal()]);
		new StoreGlobalDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.StoreGlobal.ordinal()]);
		new StoreGlobalWithVectorDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.StoreGlobalWithVector.ordinal()]);
		new StoreWithVectorDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.StoreWithVector.ordinal()]);
		new StoreTransitionDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.StoreTransition.ordinal()]);
		new StringAtDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.StringAt.ordinal()]);
		new StringAtAsStringDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.StringAtAsString.ordinal()]);
		new StringSubstringDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.StringSubstring.ordinal()]);
		new TypeConversionDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.TypeConversion.ordinal()]);
		new TypeConversionStackParameterDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.TypeConversionStackParameter.ordinal()]);
		new TypeofDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.Typeof.ordinal()]);
		new VoidDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.Void.ordinal()]);
		new WasmAtomicNotifyDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.WasmAtomicNotify.ordinal()]);
		new WasmI32AtomicWaitDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.WasmI32AtomicWait.ordinal()]);
		new WasmI64AtomicWaitDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.WasmI64AtomicWait.ordinal()]);
		new WasmMemoryGrowDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.WasmMemoryGrow.ordinal()]);
		new WasmTableGetDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.WasmTableGet.ordinal()]);
		new WasmTableSetDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.WasmTableSet.ordinal()]);
		new WasmThrowDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.WasmThrow.ordinal()]);
		new FastNewClosureDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.FastNewClosure.ordinal()]);
		new CreateRegExpLiteralDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.CreateRegExpLiteral.ordinal()]);
		new CreateEmptyArrayLiteralDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.CreateEmptyArrayLiteral.ordinal()]);
		new CreateShallowArrayLiteralDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.CreateShallowArrayLiteral.ordinal()]);
		new CreateShallowObjectLiteralDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.CreateShallowObjectLiteral.ordinal()]);
		new StringIndexOfDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.StringIndexOf.ordinal()]);
		new OrderedHashTableHealIndexDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.OrderedHashTableHealIndex.ordinal()]);
		new CopyFastSmiOrObjectElementsDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.CopyFastSmiOrObjectElements.ordinal()]);
		new EnqueueMicrotaskDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.EnqueueMicrotask.ordinal()]);
		new HasPropertyDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.HasProperty.ordinal()]);
		new DeletePropertyDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.DeleteProperty.ordinal()]);
		new CopyDataPropertiesDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.CopyDataProperties.ordinal()]);
		new SetDataPropertiesDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.SetDataProperties.ordinal()]);
		new ArrayIncludesSmiOrObjectDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.ArrayIncludesSmiOrObject.ordinal()]);
		new ArrayIncludesPackedDoublesDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.ArrayIncludesPackedDoubles.ordinal()]);
		new ArrayIncludesHoleyDoublesDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.ArrayIncludesHoleyDoubles.ordinal()]);
		new ArrayIndexOfSmiOrObjectDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.ArrayIndexOfSmiOrObject.ordinal()]);
		new ArrayIndexOfPackedDoublesDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.ArrayIndexOfPackedDoubles.ordinal()]);
		new ArrayIndexOfHoleyDoublesDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.ArrayIndexOfHoleyDoubles.ordinal()]);
		new CloneFastJSArrayDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.CloneFastJSArray.ordinal()]);
		new CloneFastJSArrayFillingHolesDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.CloneFastJSArrayFillingHoles.ordinal()]);
		new ExtractFastJSArrayDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.ExtractFastJSArray.ordinal()]);
		new FlattenIntoArrayDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.FlattenIntoArray.ordinal()]);
		new FlatMapIntoArrayDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.FlatMapIntoArray.ordinal()]);
		new AsyncFunctionEnterDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.AsyncFunctionEnter.ordinal()]);
		new AsyncFunctionRejectDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.AsyncFunctionReject.ordinal()]);
		new AsyncFunctionResolveDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.AsyncFunctionResolve.ordinal()]);
		new AsyncFunctionAwaitCaughtDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.AsyncFunctionAwaitCaught.ordinal()]);
		new AsyncFunctionAwaitUncaughtDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.AsyncFunctionAwaitUncaught.ordinal()]);
		new CreateIterResultObjectDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.CreateIterResultObject.ordinal()]);
		new CreateGeneratorObjectDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.CreateGeneratorObject.ordinal()]);
		new IterableToListDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.IterableToList.ordinal()]);
		new IterableToListWithSymbolLookupDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.IterableToListWithSymbolLookup.ordinal()]);
		new IterableToListMayPreserveHolesDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.IterableToListMayPreserveHoles.ordinal()]);
		new FindOrderedHashMapEntryDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.FindOrderedHashMapEntry.ordinal()]);
		new MapIteratorToListDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.MapIteratorToList.ordinal()]);
		new ParseIntDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.ParseInt.ordinal()]);
		new BitwiseNotDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.BitwiseNot.ordinal()]);
		new DecrementDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.Decrement.ordinal()]);
		new IncrementDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.Increment.ordinal()]);
		new NegateDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.Negate.ordinal()]);
		new CreateObjectWithoutPropertiesDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.CreateObjectWithoutProperties.ordinal()]);
		new ObjectToStringDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.ObjectToString.ordinal()]);
		new ForInEnumerateDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.ForInEnumerate.ordinal()]);
		new FulfillPromiseDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.FulfillPromise.ordinal()]);
		new RejectPromiseDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.RejectPromise.ordinal()]);
		new ResolvePromiseDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.ResolvePromise.ordinal()]);
		new NewPromiseCapabilityDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.NewPromiseCapability.ordinal()]);
		new PerformPromiseThenDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.PerformPromiseThen.ordinal()]);
		new PromiseRejectReactionJobDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.PromiseRejectReactionJob.ordinal()]);
		new PromiseFulfillReactionJobDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.PromiseFulfillReactionJob.ordinal()]);
		new PromiseResolveThenableJobDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.PromiseResolveThenableJob.ordinal()]);
		new PromiseResolveDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.PromiseResolve.ordinal()]);
		new RegExpExecAtomDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.RegExpExecAtom.ordinal()]);
		new RegExpExecInternalDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.RegExpExecInternal.ordinal()]);
		new RegExpPrototypeExecSlowDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.RegExpPrototypeExecSlow.ordinal()]);
		new RegExpSearchFastDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.RegExpSearchFast.ordinal()]);
		new RegExpSplitDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.RegExpSplit.ordinal()]);
		new SetOrSetIteratorToListDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.SetOrSetIteratorToList.ordinal()]);
		new ThrowWasmTrapUnreachableDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.ThrowWasmTrapUnreachable.ordinal()]);
		new ThrowWasmTrapMemOutOfBoundsDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.ThrowWasmTrapMemOutOfBounds.ordinal()]);
		new ThrowWasmTrapUnalignedAccessDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.ThrowWasmTrapUnalignedAccess.ordinal()]);
		new ThrowWasmTrapDivByZeroDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.ThrowWasmTrapDivByZero.ordinal()]);
		new ThrowWasmTrapDivUnrepresentableDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.ThrowWasmTrapDivUnrepresentable.ordinal()]);
		new ThrowWasmTrapRemByZeroDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.ThrowWasmTrapRemByZero.ordinal()]);
		new ThrowWasmTrapFloatUnrepresentableDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.ThrowWasmTrapFloatUnrepresentable.ordinal()]);
		new ThrowWasmTrapFuncInvalidDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.ThrowWasmTrapFuncInvalid.ordinal()]);
		new ThrowWasmTrapFuncSigMismatchDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.ThrowWasmTrapFuncSigMismatch.ordinal()]);
		new ThrowWasmTrapDataSegmentDroppedDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.ThrowWasmTrapDataSegmentDropped.ordinal()]);
		new ThrowWasmTrapElemSegmentDroppedDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.ThrowWasmTrapElemSegmentDropped.ordinal()]);
		new ThrowWasmTrapTableOutOfBoundsDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.ThrowWasmTrapTableOutOfBounds.ordinal()]);
		new WeakMapLookupHashIndexDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.WeakMapLookupHashIndex.ordinal()]);
		new WeakCollectionDeleteDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.WeakCollectionDelete.ordinal()]);
		new WeakCollectionSetDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.WeakCollectionSet.ordinal()]);
		new AsyncGeneratorResolveDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.AsyncGeneratorResolve.ordinal()]);
		new AsyncGeneratorRejectDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.AsyncGeneratorReject.ordinal()]);
		new AsyncGeneratorYieldDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.AsyncGeneratorYield.ordinal()]);
		new AsyncGeneratorReturnDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.AsyncGeneratorReturn.ordinal()]);
		new AsyncGeneratorResumeNextDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.AsyncGeneratorResumeNext.ordinal()]);
		new AsyncGeneratorAwaitCaughtDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.AsyncGeneratorAwaitCaught.ordinal()]);
		new AsyncGeneratorAwaitUncaughtDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.AsyncGeneratorAwaitUncaught.ordinal()]);
		new StringAdd_CheckNoneDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.StringAdd_CheckNone.ordinal()]);
		new SubStringDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.SubString.ordinal()]);
		new GetPropertyWithReceiverDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.GetPropertyWithReceiver.ordinal()]);
		new SetPropertyDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.SetProperty.ordinal()]);
		new SetPropertyInLiteralDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.SetPropertyInLiteral.ordinal()]);
		new StringToLowerCaseIntlDescriptor().initialize(call_descriptor_data_[CallDescriptors.Key.StringToLowerCaseIntl.ordinal()]);
		
		
		
	}
};