package org.jsengine.v8.internal.torque; 

import org.jsengine.v8.internal.Torque;

import javax.xml.transform.Source;
import java.util.Vector;
import java.util.Map;
import java.util.HashMap;

public class GlobalContext {
	public static ThreadLocal<Scope> top_ = new ThreadLocal<Scope>();
	private boolean collect_language_server_data_;
	private boolean force_assert_statements_;
	private Namespace default_namespace_;
	private Ast ast_;
	private Vector<Declarable> declarables_ = new Vector<Declarable>();
	private GlobalClassList classes_ = new GlobalClassList();
	private int fresh_id_ = 0;
	private Vector<String> cpp_includes_ = new Vector<String>();
	private Map<SourceId, PerFileStreams> generated_per_file_ = new HashMap<SourceId, PerFileStreams>();
	
	public GlobalContext(Ast ast) {
		collect_language_server_data_ = false;
		force_assert_statements_ = false;
		ast_ = ast;
		CurrentScope.Scope current_scope = new CurrentScope.Scope(null);
		CurrentSourcePosition.Scope current_source_position = 
			new CurrentSourcePosition.Scope(
				new SourcePosition(CurrentSourceFile.get(),
					new LineAndColumn(-1, -1),
					new LineAndColumn(-1, -1)));
		default_namespace_ = registerDeclarable(new Namespace(Torque.kBaseNamespaceName));
	}
	
	public static Namespace getDefaultNamespace() {
		return get().default_namespace_;
	}
	
	public static class Scope {
		public GlobalContext value_;
		public Scope previous_;
		
		public Scope(Ast value) {
			value_ = new GlobalContext(value);
			previous_ = top();
			top(this);
		}
		
		public GlobalContext value() {
			return value_;
		}
	}
	
	public static Scope top() {
		return top_.get();
	}
		
	public static void top(Scope scope) {
		top_.set(scope);
	}
	
	public static GlobalContext get() {
		return top().value();
	}
	
	public <T> T registerDeclarable(T d) {
		declarables_.add((Declarable) d);
		return d;
	}
	
	private static class GlobalClassList extends Vector<TypeAlias> {
		public GlobalClassList() {
			super();
		}
	}
	
	public static void setCollectLanguageServerData() {
		get().collect_language_server_data_ = true;
	}
	
	public static void setForceAssertStatements() {
		get().force_assert_statements_ = true;
	}
	
	public static Ast ast() {
		return get().ast_;
	}
	
	public static Vector<Declarable> allDeclarables() {
		return get().declarables_;
	}
	
	public static boolean collect_language_server_data() {
		return get().collect_language_server_data_;
	}

	public static int freshId() {
		return get().fresh_id_++;
	}

	public static void addCppInclude(String include_path) {
		get().cpp_includes_.add(include_path);
	}

	public static class PerFileStreams {
		StringBuilder csa_headerfile;
		StringBuilder csa_ccfile;

		public PerFileStreams(StringBuilder csa_headerfile, StringBuilder csa_ccfile) {
			this.csa_headerfile = csa_headerfile;
			this.csa_ccfile = csa_ccfile;
		}
	};

	public static PerFileStreams generatedPerFile(SourceId file) {
		return get().generated_per_file_.get(file);
	}

	public static Vector<String> cppIncludes() {
		return get().cpp_includes_;
	}
}