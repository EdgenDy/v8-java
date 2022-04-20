package org.jsengine.v8;

// include\v8.h:6500
public abstract class Extension {
	private String name_;
	private int source_length_;
	private int dep_count_;
	private String deps_[];
	private boolean auto_enable_;
	private String$.ExternalOneByteStringResource source_;
	
	// src\api\api.cc:958
	public Extension(String name, String source, int dep_count, String deps[], int source_length) {
		name_ = name;
		source_length_ = (source_length >= 0 ? source_length : (source != null ? source.length() : 0));
		dep_count_ = dep_count;
		deps_ = deps;
		auto_enable_ = false;
		
		source_ = new ExtensionResource(source, source_length_);
	}
	
	// src\api\api.cc:958
	public Extension(String name, String source, int dep_count, String deps[]) {
		this(name, source, dep_count, deps, -1);
	}
	
	// src\api\api.cc:958
	public Extension(String name, String source, int dep_count) {
		this(name, source, dep_count, null, -1);
	}
	
	// src\api\api.cc:958
	public Extension(String name, String source) {
		this(name, source, 0, null, -1);
	}
	
	// src\api\api.cc:958
	public Extension(String name) {
		this(name, null, 0, null, -1);
	}
}