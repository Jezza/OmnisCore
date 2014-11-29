package me.jezza.oc.api.configuration.entries;

import me.jezza.oc.api.configuration.Config.ConfigInteger;
import net.minecraftforge.common.config.Configuration;

import java.lang.reflect.Field;

public class ConfigEntryInteger extends ConfigEntry<ConfigInteger> {
    @Override
    public void processAnnotation(Configuration config, Field field, ConfigInteger annotation) throws IllegalAccessException {
        int value = config.getInt(field.getName(), annotation.category(), annotation.defaultValue(), annotation.minValue(), annotation.maxValue(), annotation.comment());
        field.set(null, value);
    }
}
