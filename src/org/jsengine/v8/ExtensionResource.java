package org.jsengine.v8;

public class ExtensionResource extends String$.ExternalOneByteStringResource {
	private String data_;
	private int length_;
	
	public ExtensionResource() {
		data_ = null;
		length_ = 0;
	}
	
	public ExtensionResource(String data, int length) {
		data_ = data;
		length_ = length;
	}
}

