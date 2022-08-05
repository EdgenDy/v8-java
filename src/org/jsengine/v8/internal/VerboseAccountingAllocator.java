package org.jsengine.v8.internal;

public class VerboseAccountingAllocator extends AccountingAllocator {
	private Heap heap_;
	private long allocation_sample_bytes_ = 0;
	public VerboseAccountingAllocator(Heap heap, long allocation_sample_bytes) {
		heap_ = heap;
		allocation_sample_bytes_ = allocation_sample_bytes;
	}
}