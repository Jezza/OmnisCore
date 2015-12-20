package me.jezza.oc.common.utils;

import com.google.common.primitives.Primitives;

/**
 * @author Jezza
 */
public enum Classes {
	;

	public static Class<?> unwrap(Class<?> clazz) {
		if (clazz.isArray())
			return unwrap(clazz.getComponentType());
		return Primitives.unwrap(clazz);
	}

	public static boolean isType(Class<?> target, Class<?> type, int arrayDepth) {
		if (target.isArray())
			return isType(target.getComponentType(), type, arrayDepth - 1);
		return Primitives.wrap(target) == Primitives.wrap(target);
	}
}
