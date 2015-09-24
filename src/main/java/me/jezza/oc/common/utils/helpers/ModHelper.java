package me.jezza.oc.common.utils.helpers;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;

import java.util.Map;

/**
 * @author Jezza
 */
public class ModHelper {

    private static Map<String, ModContainer> indexedModMap;

    /**
     * This way only one unmodifiable list is created.
     * @return A ImmutableMap of all mods, mapped to their modId.
     */
    public static Map<String, ModContainer> getIndexedModMap() {
        return indexedModMap != null ? indexedModMap : (indexedModMap = Loader.instance().getIndexedModList());
    }
}
