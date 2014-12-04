package me.jezza.oc.api.configuration.entries;

import me.jezza.oc.api.configuration.Config.ConfigBoolean;
import me.jezza.oc.api.configuration.ConfigEntry;
import net.minecraftforge.common.config.Configuration;

public class ConfigEntryBoolean extends ConfigEntry<ConfigBoolean, Boolean> {
    @Override
    public Object processAnnotation(Configuration config, String fieldName, ConfigBoolean annotation, Boolean defaultValue) {
        String comment = processComment(annotation.comment());
        return config.getBoolean(fieldName, annotation.category(), defaultValue, comment);
    }
}
