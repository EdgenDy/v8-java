package org.jsengine.v8.platform;

import org.jsengine.v8.base.AtomicWord;

public class Tracing {
	public static int kMaxCategoryGroups = 200;
	public static String[] g_category_groups = new String[kMaxCategoryGroups];
	static {
		g_category_groups[0] = "toplevel";
    	g_category_groups[1] = "tracing categories exhausted; must increase kMaxCategoryGroups";
    	g_category_groups[2] = "__metadata";
	};
	public static int[] g_category_group_enabled = new int[kMaxCategoryGroups];
	
	public static int g_category_categories_exhausted = 1;
	
	public static int g_num_builtin_categories = 3;
	
	public static AtomicWord g_category_index = new AtomicWord(g_num_builtin_categories);
}