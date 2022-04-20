package org.jsengine.v8.internal;

// src\objects\elements.h:19
public class ElementsAccessor {
    private static ElementsAccessor[] elements_accessors_;
    private static ElementsAccessor[] accessor_array;

	// src\objects\elements.h:105
	// src\objects\elements.cc:4777
    public static void initializeOncePerProcess() {
        if (accessor_array == null) {
            accessor_array = new ElementsAccessor[]{
                    new FastPackedSmiElementsAccessor(),
                    new FastHoleySmiElementsAccessor(),
                    new FastPackedObjectElementsAccessor(),
                    new FastHoleyObjectElementsAccessor(),
                    new FastPackedDoubleElementsAccessor(),
                    new FastHoleyDoubleElementsAccessor(),
                    new FastPackedNonextensibleObjectElementsAccessor(),
                    new FastHoleyNonextensibleObjectElementsAccessor(),
					new FastPackedSealedObjectElementsAccessor(),
					new FastHoleySealedObjectElementsAccessor(),
					new FastPackedFrozenObjectElementsAccessor(),
					new FastHoleyFrozenObjectElementsAccessor(),
					new DictionaryElementsAccessor(),
					new FastSloppyArgumentsElementsAccessor(),
					new SlowSloppyArgumentsElementsAccessor(),
					new FastStringWrapperElementsAccessor(),
					new SlowStringWrapperElementsAccessor(),
					new Uint8ElementsAccessor(),
					new Int8ElementsAccessor(),
					new Uint16ElementsAccessor(),
					new Int16ElementsAccessor(),
					new Uint32ElementsAccessor(),
					new Int32ElementsAccessor(),
					new Float32ElementsAccessor(),
					new Float64ElementsAccessor(),
					new Uint8ClampedElementsAccessor(),
					new BigUint64ElementsAccessor(),
					new BigInt64ElementsAccessor()
            };
        }

        elements_accessors_ = accessor_array;
    }
}