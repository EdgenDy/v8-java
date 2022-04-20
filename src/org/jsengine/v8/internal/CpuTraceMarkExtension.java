package org.jsengine.v8.internal;

import org.jsengine.v8.Extension;
import org.jsengine.v8.Internal;

public class CpuTraceMarkExtension extends Extension {
	private static char buffer_[] = new char[50];
	public CpuTraceMarkExtension(String fun_name) {
		super("v8/gc", buildSource(buffer_, buffer_.length, fun_name));
	}
	
	private static String buildSource(char[] buf, int size, String fun_name) {
		Internal.sNPrintF(new Vector(buf, buf.length), "native function %s();", fun_name);
		System.out.println(new String(buf));
		return new String(buf);
	}
}