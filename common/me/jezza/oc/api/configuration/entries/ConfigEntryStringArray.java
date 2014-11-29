package me.jezza.oc.api.configuration.entries;

import me.jezza.oc.api.configuration.Config.ConfigStringArray;
import net.minecraftforge.common.config.Configuration;

public class ConfigEntryStringArray extends ConfigEntry<ConfigStringArray> {
    @Override
    public Object processAnnotation(Configuration config, String fieldName, ConfigStringArray annotation) {
        String[] validValues = annotation.validValues();
        if (validValues.length == 0)
            return config.getStringList(fieldName, annotation.category(), annotation.defaultArray(), annotation.comment());
        return config.getStringList(fieldName, annotation.category(), annotation.defaultArray(), annotation.comment(), validValues);
    }
}
