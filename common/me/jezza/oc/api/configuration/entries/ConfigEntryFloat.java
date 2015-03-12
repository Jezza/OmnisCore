package me.jezza.oc.api.configuration.entries;

import me.jezza.oc.api.configuration.Config.ConfigFloat;
import me.jezza.oc.api.configuration.ConfigEntry;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class ConfigEntryFloat extends ConfigEntry<ConfigFloat, Float> {
    @Override
    public Object loadAnnotation(Configuration config, String fieldName, ConfigFloat annotation, Float currentValue, Float defaultValue) {
        String comment = processComment(annotation.comment());
        return config.getFloat(fieldName, annotation.category(), defaultValue, annotation.minValue(), annotation.maxValue(), comment);
    }

    @Override
    public void saveAnnotation(Configuration config, String fieldName, ConfigFloat annotation, Float currentValue, Float defaultValue) {
        String comment = processComment(annotation.comment());
        Property prop = config.get(annotation.category(), fieldName, Float.toString(defaultValue), comment);
        prop.comment += " [range: " + annotation.minValue() + " ~ " + annotation.maxValue() + ", default: " + defaultValue + "]";
        prop.setMinValue(annotation.minValue());
        prop.setMaxValue(annotation.maxValue());
        prop.set(Float.toString(currentValue));
    }
}
