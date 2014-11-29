package me.jezza.oc.api.configuration.entries;

import me.jezza.oc.api.configuration.Config.ConfigIntegerArray;
import net.minecraftforge.common.config.Configuration;

import java.lang.reflect.Field;

public class ConfigEntryIntegerArray extends ConfigEntry<ConfigIntegerArray> {
    @Override
    public void processAnnotation(Configuration config, Field field, ConfigIntegerArray annotation) throws IllegalAccessException {
        int[] values = config.get(annotation.category(), field.getName(), annotation.defaultArray(), annotation.comment(), annotation.minValue(), annotation.maxValue(), annotation.isListLengthFixed(), annotation.maxListLength()).getIntList();
        field.set(null, values);
    }
}
