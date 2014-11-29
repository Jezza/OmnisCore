package me.jezza.oc.api.configuration.entries;

import me.jezza.oc.api.configuration.Config.ConfigBooleanArray;
import net.minecraftforge.common.config.Configuration;

public class ConfigEntryBooleanArray extends ConfigEntry<ConfigBooleanArray> {
    @Override
    public Object processAnnotation(Configuration config, String fieldName, ConfigBooleanArray annotation) {
        return config.get(annotation.category(), fieldName, annotation.defaultArray(), annotation.comment(), annotation.isListLengthFixed(), annotation.maxListLength()).getBooleanList();
    }
}
