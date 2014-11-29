package me.jezza.oc.api.configuration.entries;

import me.jezza.oc.api.configuration.Config.ConfigBoolean;
import net.minecraftforge.common.config.Configuration;

public class ConfigEntryBoolean extends ConfigEntry<ConfigBoolean> {
    @Override
    public Object processAnnotation(Configuration config, String fieldName, ConfigBoolean annotation) {
        return config.getBoolean(fieldName, annotation.category(), annotation.defaultValue(), annotation.comment());
    }
}
