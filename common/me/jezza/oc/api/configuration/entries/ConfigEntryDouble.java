package me.jezza.oc.api.configuration.entries;

import me.jezza.oc.api.configuration.Config.ConfigDouble;
import me.jezza.oc.api.configuration.ConfigEntry;
import net.minecraftforge.common.config.Configuration;

public class ConfigEntryDouble extends ConfigEntry<ConfigDouble, Double> {
    @Override
    public Object loadAnnotation(Configuration config, String fieldName, ConfigDouble annotation, Double currentValue, Double defaultValue) {
        String comment = processComment(annotation.comment());
        return config.get(annotation.category(), fieldName, defaultValue, comment, annotation.minValue(), annotation.maxValue()).getDouble();
    }

    @Override
    public void saveAnnotation(Configuration config, String fieldName, ConfigDouble annotation, Double currentValue, Double defaultValue) {
        String comment = processComment(annotation.comment());
        config.get(annotation.category(), fieldName, defaultValue, comment, annotation.minValue(), annotation.maxValue()).set(currentValue);
    }
}
