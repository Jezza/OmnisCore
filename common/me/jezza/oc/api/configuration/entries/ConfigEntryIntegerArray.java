package me.jezza.oc.api.configuration.entries;

import me.jezza.oc.api.configuration.Config.ConfigIntegerArray;
import me.jezza.oc.api.configuration.ConfigEntry;
import net.minecraftforge.common.config.Configuration;

public class ConfigEntryIntegerArray extends ConfigEntry<ConfigIntegerArray, int[]> {
    @Override
    public Object processAnnotation(Configuration config, String fieldName, ConfigIntegerArray annotation, int[] defaultValues) {
        return config.get(annotation.category(), fieldName, defaultValues, annotation.comment(), annotation.minValue(), annotation.maxValue(), annotation.isListLengthFixed(), annotation.maxListLength()).getIntList();
    }
}
