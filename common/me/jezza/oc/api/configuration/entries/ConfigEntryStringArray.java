package me.jezza.oc.api.configuration.entries;

import me.jezza.oc.api.configuration.Config.ConfigStringArray;
import me.jezza.oc.api.configuration.ConfigEntry;
import net.minecraftforge.common.config.Configuration;

public class ConfigEntryStringArray extends ConfigEntry<ConfigStringArray, String[]> {
    @Override
    public Object loadAnnotation(Configuration config, String fieldName, ConfigStringArray annotation, String[] currentValue, String[] defaultValue) {
        String comment = processComment(annotation.comment());
        String[] validValues = annotation.validValues();
        if (validValues.length == 0)
            return config.getStringList(fieldName, annotation.category(), defaultValue, comment);
        return config.getStringList(fieldName, annotation.category(), defaultValue, comment, validValues);
    }

    @Override
    public void saveAnnotation(Configuration config, String fieldName, ConfigStringArray annotation, String[] currentValue, String[] defaultValue) {
        String comment = processComment(annotation.comment());
        config.get(annotation.category(), fieldName, defaultValue, comment).set(currentValue);
    }
}
