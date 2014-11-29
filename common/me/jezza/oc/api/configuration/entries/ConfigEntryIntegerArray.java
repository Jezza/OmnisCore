package me.jezza.oc.api.configuration.entries;

import me.jezza.oc.api.configuration.Config.ConfigIntegerArray;
import net.minecraftforge.common.config.Configuration;

public class ConfigEntryIntegerArray extends ConfigEntry<ConfigIntegerArray> {
    @Override
    public Object processAnnotation(Configuration config, String fieldName, ConfigIntegerArray annotation) {
        return config.get(annotation.category(), fieldName, annotation.defaultArray(), annotation.comment(), annotation.minValue(), annotation.maxValue(), annotation.isListLengthFixed(), annotation.maxListLength()).getIntList();
    }
}
