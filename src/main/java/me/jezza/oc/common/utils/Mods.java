package me.jezza.oc.common.utils;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;

import java.util.Map;

/**
 * @author Jezza
 */
public enum Mods {
	;
	private static Map<String, ModContainer> indexedModMap;

	/**
	 * This way only one unmodifiable list is created.
	 *
	 * @return A ImmutableMap of all mods, mapped to their modId.
	 */
	public static Map<String, ModContainer> map() {
		return indexedModMap != null ? indexedModMap : (indexedModMap = Loader.instance().getIndexedModList());
	}

	public static ModContainer get(String modId) {
		return map().get(modId);
	}
}
