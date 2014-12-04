package me.jezza.oc.api.configuration.entries;

import me.jezza.oc.api.configuration.Config.ConfigString;
import me.jezza.oc.api.configuration.ConfigEntry;
import net.minecraftforge.common.config.Configuration;

public class ConfigEntryString extends ConfigEntry<ConfigString, String> {
    @Override
    public Object processAnnotation(Configuration config, String fieldName, ConfigString annotation, String defaultValue) {
        String comment = processComment(annotation.comment());
        String[] validValues = annotation.validValues();
        if (validValues.length == 0)
            return config.getString(fieldName, annotation.category(), defaultValue, comment);
        return config.getString(fieldName, annotation.category(), defaultValue, comment, validValues);
    }
}
