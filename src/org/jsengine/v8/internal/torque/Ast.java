package org.jsengine.v8.internal.torque;

import java.util.HashMap;
import java.util.Vector;
import java.util.Map;
import java.util.Set;

public class Ast {
	private Map<SourceId, Set<SourceId>> declared_imports_ = new HashMap<SourceId, Set<SourceId>>();
	private Vector<Declaration> declarations_ = new Vector<Declaration>();
	private Vector<AstNode> nodes_ = new Vector<AstNode>();
	
	public Ast() {
		
	}
	
	public void declareImportForCurrentFile(SourceId import_id) {
		declared_imports_.get(CurrentSourcePosition.get().source).add(import_id);
	}
	
	public Vector<Declaration> declarations() {
		return declarations_;
	}

	public <T> T addNode(T node) {
		nodes_.add((AstNode) node);
		return node;
	}
}