package org.jsengine.utils;

import java.util.HashSet;

public class UnorderedSet<T> extends HashSet<T> {
	public UnorderedSet() {
		super();
	}
	
	public T find(T object) {
		for (T each : this) {
			if (object == each)
				return each;
		}
		return null;
	}
}