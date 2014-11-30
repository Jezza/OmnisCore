package me.jezza.oc.api.configuration.entries;

import me.jezza.oc.api.configuration.Config.ConfigFloat;
import me.jezza.oc.api.configuration.ConfigEntry;
import net.minecraftforge.common.config.Configuration;

public class ConfigEntryFloat extends ConfigEntry<ConfigFloat, Float> {
    @Override
    public Object processAnnotation(Configuration config, String fieldName, ConfigFloat annotation, Float defaultValue) {
        return config.getFloat(fieldName, annotation.category(), defaultValue, annotation.minValue(), annotation.maxValue(), annotation.comment());
    }
}
