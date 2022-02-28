package org.jsengine.v8.internal;

import org.jsengine.v8.PageAllocator;
import org.jsengine.v8.base.LeakyObject;

public class PageAllocatorInitializer {
	private PageAllocator page_allocator_;
	public static LeakyObject<org.jsengine.v8.base.PageAllocator> default_page_allocator;
	public PageAllocatorInitializer() {
		page_allocator_ = V8.getCurrentPlatform().getPageAllocator();
		if(page_allocator_ == null) {
			if(default_page_allocator == null)
				default_page_allocator = new LeakyObject<org.jsengine.v8.base.PageAllocator>(new org.jsengine.v8.base.PageAllocator());
		}
	}
	
	public PageAllocator page_allocator() {
		return page_allocator_;
	}
}