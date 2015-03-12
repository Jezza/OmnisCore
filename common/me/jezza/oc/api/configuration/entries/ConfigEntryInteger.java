package me.jezza.oc.api.configuration.entries;

import me.jezza.oc.api.configuration.Config.ConfigInteger;
import me.jezza.oc.api.configuration.ConfigEntry;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class ConfigEntryInteger extends ConfigEntry<ConfigInteger, Integer> {
    @Override
    public Object loadAnnotation(Configuration config, String fieldName, ConfigInteger annotation, Integer currentValue, Integer defaultValue) {
        String comment = processComment(annotation.comment());
        return config.getInt(fieldName, annotation.category(), defaultValue, annotation.minValue(), annotation.maxValue(), comment);
    }

    @Override
    public void saveAnnotation(Configuration config, String fieldName, ConfigInteger annotation, Integer currentValue, Integer defaultValue) {
        String comment = processComment(annotation.comment());
        Property prop = config.get(annotation.category(), fieldName, defaultValue, comment, annotation.minValue(), annotation.maxValue());
        prop.setLanguageKey(fieldName);
        prop.comment += " [range: " + annotation.minValue() + " ~ " + annotation.maxValue() + ", default: " + defaultValue + "]";
        prop.set(currentValue);
    }
}
