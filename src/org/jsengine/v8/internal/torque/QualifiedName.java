package org.jsengine.v8.internal.torque; 

import java.util.Vector;

public class QualifiedName {
	Vector<String> namespace_qualification;
	String name;

	public QualifiedName(Vector<String> namespace_qualification, String name) {
		namespace_qualification = namespace_qualification;
        name = name;
	}
	
	public QualifiedName(String name) {
		this(new Vector<String>(), name);
	}
};