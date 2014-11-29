package me.jezza.oc.api.configuration.entries;

import me.jezza.oc.api.configuration.Config.ConfigFloat;
import net.minecraftforge.common.config.Configuration;

public class ConfigEntryFloat extends ConfigEntry<ConfigFloat> {
    @Override
    public Object processAnnotation(Configuration config, String fieldName, ConfigFloat annotation) {
        return config.getFloat(fieldName, annotation.category(), annotation.defaultValue(), annotation.minValue(), annotation.maxValue(), annotation.comment());
    }
}
