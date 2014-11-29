package me.jezza.oc.api.configuration.entries;

import me.jezza.oc.api.configuration.Config.ConfigString;
import net.minecraftforge.common.config.Configuration;

public class ConfigEntryString extends ConfigEntry<ConfigString> {
    @Override
    public Object processAnnotation(Configuration config, String fieldName, ConfigString annotation) {
        String[] validValues = annotation.validValues();
        if (validValues.length == 0)
            return config.getString(fieldName, annotation.category(), annotation.defaultValue(), annotation.comment());
        return config.getString(fieldName, annotation.category(), annotation.defaultValue(), annotation.comment(), validValues);
    }
}
