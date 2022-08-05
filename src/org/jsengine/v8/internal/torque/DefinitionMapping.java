package org.jsengine.v8.internal.torque; 

import java.util.AbstractMap;

public class DefinitionMapping extends AbstractMap.SimpleEntry<SourcePosition, SourcePosition> {
	public DefinitionMapping(SourcePosition key, SourcePosition value) {
		super(key, value);
	}
}