package me.jezza.oc.api.configuration.entries;

import me.jezza.oc.api.configuration.Config.ConfigInteger;
import net.minecraftforge.common.config.Configuration;

public class ConfigEntryInteger extends ConfigEntry<ConfigInteger> {
    @Override
    public Object processAnnotation(Configuration config, String fieldName, ConfigInteger annotation) {
        return config.getInt(fieldName, annotation.category(), annotation.defaultValue(), annotation.minValue(), annotation.maxValue(), annotation.comment());
    }
}
