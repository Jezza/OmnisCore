package me.jezza.oc.api.configuration.entries;

import me.jezza.oc.api.configuration.Config.ConfigFloat;
import net.minecraftforge.common.config.Configuration;

import java.lang.reflect.Field;

public class ConfigEntryFloat extends ConfigEntry<ConfigFloat> {
    @Override
    public void processAnnotation(Configuration config, Field field, ConfigFloat annotation) throws IllegalAccessException {
        float value = config.getFloat(field.getName(), annotation.category(), annotation.defaultValue(), annotation.minValue(), annotation.maxValue(), annotation.comment());
        field.set(null, value);
    }
}
