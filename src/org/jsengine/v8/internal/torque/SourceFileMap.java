package org.jsengine.v8.internal.torque; 

import org.jsengine.v8.internal.Torque;
import java.util.Vector;
import java.io.File;

public class SourceFileMap {
	public static ThreadLocal<Scope> top_ = new ThreadLocal<Scope>();
	private Vector<String> sources_ = new Vector<String>();
	private String v8_root_;
	
	public SourceFileMap(String v8_root) {
		v8_root_ = v8_root;
	}
  
	public static class Scope {
		public SourceFileMap value_;
		public Scope previous_;
		
		public Scope(SourceFileMap value) {
			value_ = value;
			previous_ = top();
			top(this);
		}
		
		public SourceFileMap value() {
			return value_;
		}
	}
	
	public static Scope top() {
		return top_.get();
	}
		
	public static void top(Scope scope) {
		top_.set(scope);
	}
	
	public static SourceFileMap get() {
		return top().value();
	}
	
	public static SourceId addSource(String path) {
		get().sources_.add(path);
		return new SourceId(get().sources_.size() - 1);
	}
	
	public static String pathFromV8Root(SourceId file) {
		return get().sources_.get(file.id_);
	}
	
	public static String absolutePath(SourceId file) {
		String root_path = pathFromV8Root(file);
		if (root_path.startsWith("file://"))
			return root_path;
				
		return get().v8_root_ + "/" + pathFromV8Root(file);
	}
	
	public static boolean fileRelativeToV8RootExists(String path) {
		String file = get().v8_root_ + "/" + path;
		return (new File(file)).exists();
	}
	
	public static SourceId getSourceId(String path) {
		for (int i = 0; i < get().sources_.size(); ++i) {
			if (get().sources_.get(i) == path) {
				return new SourceId(i);
			}
		}
		return SourceId.invalid();
	}

	public static Vector<SourceId> allSources() {
		SourceFileMap self = get();
		Vector<SourceId> result = new Vector<SourceId>();
		for (int i = 0; i < self.sources_.size(); ++i) {
			result.add(new SourceId(i));
		}
		return result;
	}

	public static String pathFromV8RootWithoutExtension(SourceId file) {
		String path_from_root = pathFromV8Root(file);
		if (!Torque.stringEndsWith(path_from_root, ".tq")) {
			Torque.error("Not a .tq file: ", path_from_root).Throw();
		}
		path_from_root = path_from_root.substring(0, path_from_root.length() - ".tq".length());
		return path_from_root;
	}
}