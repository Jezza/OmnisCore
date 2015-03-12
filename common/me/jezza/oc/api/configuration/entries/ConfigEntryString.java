package me.jezza.oc.api.configuration.entries;

import me.jezza.oc.api.configuration.Config.ConfigString;
import me.jezza.oc.api.configuration.ConfigEntry;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class ConfigEntryString extends ConfigEntry<ConfigString, String> {
    @Override
    public Object loadAnnotation(Configuration config, String fieldName, ConfigString annotation, String currentValue, String defaultValue) {
        String comment = processComment(annotation.comment());
        String[] validValues = annotation.validValues();
        return validValues.length == 0 ? config.getString(fieldName, annotation.category(), defaultValue, comment) : config.getString(fieldName, annotation.category(), defaultValue, comment, validValues);
    }

    @Override
    public void saveAnnotation(Configuration config, String fieldName, ConfigString annotation, String currentValue, String defaultValue) {
        String comment = processComment(annotation.comment());
        Property prop = config.get(annotation.category(), fieldName, defaultValue);
        prop.setLanguageKey(fieldName);

        String[] validValues = annotation.validValues();
        if (validValues.length > 0)
            prop.setValidValues(validValues);

        prop.comment = comment + " [default: " + defaultValue + "]";
        prop.set(currentValue);
    }
}
