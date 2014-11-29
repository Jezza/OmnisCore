package me.jezza.oc.api.configuration.entries;

import me.jezza.oc.api.configuration.Config.ConfigDoubleArray;
import net.minecraftforge.common.config.Configuration;

public class ConfigEntryDoubleArray extends ConfigEntry<ConfigDoubleArray> {
    @Override
    public Object processAnnotation(Configuration config, String fieldName, ConfigDoubleArray annotation) {
        return config.get(annotation.category(), fieldName, annotation.defaultArray(), annotation.comment(), annotation.minValue(), annotation.maxValue(), annotation.isListLengthFixed(), annotation.maxListLength()).getDoubleList();
    }
}
