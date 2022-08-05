package org.jsengine.v8.internal.torque; 

import java.util.HashMap;

public class LanguageServerData {
	public static ThreadLocal<Scope> top_ = new ThreadLocal<Scope>();
	private DefinitionsMap definitions_map_ = new DefinitionsMap();
	
	public static class Scope {
		public LanguageServerData value_;
		public Scope previous_;
		
		public Scope(LanguageServerData value) {
			value_ = value;
			previous_ = top();
			top(this);
		}
		
		public LanguageServerData value() {
			return value_;
		}
	}
	
	public static Scope top() {
		return top_.get();
	}
		
	public static void top(Scope scope) {
		top_.set(scope);
	}
	
	public static LanguageServerData get() {
		return top().value();
	}
	
	public static void addDefinition(SourcePosition token, SourcePosition definition) {
		(get().definitions_map_).get(token.source).add(new DefinitionMapping(token, definition));
	}
}