package org.jsengine.v8.internal;

import org.jsengine.v8.PageAllocator;
import org.jsengine.Globals;

public class VirtualMemory {
	private PageAllocator page_allocator_ = null;
	public VirtualMemory(PageAllocator page_allocator, long size, Object hint, long alignment) {
		page_allocator_ = page_allocator;
		int page_size = page_allocator_.allocatePageSize();
		alignment = Globals.roundUp(alignment, page_size);
	}
	
	public VirtualMemory(PageAllocator page_allocator, long size, Object hint) {
		this(page_allocator, size, hint, 1);         	
	}
}