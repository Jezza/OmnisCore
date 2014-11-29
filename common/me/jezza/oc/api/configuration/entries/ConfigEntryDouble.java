package me.jezza.oc.api.configuration.entries;

import me.jezza.oc.api.configuration.Config.ConfigDouble;
import net.minecraftforge.common.config.Configuration;

public class ConfigEntryDouble extends ConfigEntry<ConfigDouble> {
    @Override
    public Object processAnnotation(Configuration config, String fieldName, ConfigDouble annotation) {
        return config.get(annotation.category(), fieldName, annotation.defaultValue(), annotation.comment(), annotation.minValue(), annotation.maxValue()).getDouble();
    }
}
