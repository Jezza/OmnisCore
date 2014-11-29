package me.jezza.oc.api.configuration.entries;

import me.jezza.oc.api.configuration.Config.ConfigDouble;
import net.minecraftforge.common.config.Configuration;

import java.lang.reflect.Field;

public class ConfigEntryDouble extends ConfigEntry<ConfigDouble> {
    @Override
    public void processAnnotation(Configuration config, Field field, ConfigDouble annotation) throws IllegalAccessException {
        double value = config.get(annotation.category(), field.getName(), annotation.defaultValue(), annotation.comment(), annotation.minValue(), annotation.maxValue()).getDouble();
        field.set(null, value);
    }
}
