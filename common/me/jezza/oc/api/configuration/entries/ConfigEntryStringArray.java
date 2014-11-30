package me.jezza.oc.api.configuration.entries;

import me.jezza.oc.api.configuration.Config.ConfigStringArray;
import me.jezza.oc.api.configuration.ConfigEntry;
import net.minecraftforge.common.config.Configuration;

public class ConfigEntryStringArray extends ConfigEntry<ConfigStringArray, String[]> {
    @Override
    public Object processAnnotation(Configuration config, String fieldName, ConfigStringArray annotation, String[] defaultValues) {
        String[] validValues = annotation.validValues();
        if (validValues.length == 0)
            return config.getStringList(fieldName, annotation.category(), defaultValues, annotation.comment());
        return config.getStringList(fieldName, annotation.category(), defaultValues, annotation.comment(), validValues);
    }
}
