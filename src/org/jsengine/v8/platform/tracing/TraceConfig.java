package org.jsengine.v8.platform.tracing;

import java.util.Iterator;
import java.util.Vector;

public class TraceConfig {
	private boolean enable_systrace_;
	private boolean enable_argument_filter_;
	private StringList included_categories_;
	
	public TraceConfig() {
		enable_systrace_ = false;
		enable_argument_filter_ = false;
	}
	
	public boolean isCategoryGroupEnabled(String category_group) {
		String[] category_stream = category_group.split(",");
		int index = 0;
		while(index < category_stream.length) {
			String category = category_stream[index];
			Iterator vector_iterator = included_categories_.iterator();
			
			for(;vector_iterator.hasNext();) {
				String included_category = (String) vector_iterator.next();
				if(category == included_category) 
					return true;
			}
			index++;
		}
		return false;
	}
	
	private static class StringList extends Vector<String> {
		public StringList() {
			super();
		}
	}
}
