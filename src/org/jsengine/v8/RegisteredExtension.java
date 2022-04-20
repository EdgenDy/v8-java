package org.jsengine.v8;

public class RegisteredExtension {
	private Extension extension_;
	private RegisteredExtension next_ = null;
	private static RegisteredExtension first_extension_ = null;
	
	private RegisteredExtension(Extension extension) {
		this.extension_ = extension;
	}
	public static void register(Extension extension) {
		RegisteredExtension new_extension = new RegisteredExtension(extension);
		new_extension.next_ = first_extension_;
		first_extension_ = new_extension;
	}
}