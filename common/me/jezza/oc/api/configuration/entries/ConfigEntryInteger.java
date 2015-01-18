package me.jezza.oc.api.configuration.entries;

import me.jezza.oc.api.configuration.Config.ConfigInteger;
import me.jezza.oc.api.configuration.ConfigEntry;
import net.minecraftforge.common.config.Configuration;

public class ConfigEntryInteger extends ConfigEntry<ConfigInteger, Integer> {
    @Override
    public Object processAnnotation(Configuration config, String fieldName, ConfigInteger annotation, Integer defaultValue) {
        String comment = processComment(annotation.comment());
        
        return config.getInt(fieldName, annotation.category(), defaultValue, annotation.minValue(), annotation.maxValue(), comment);
    }
}
