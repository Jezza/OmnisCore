package me.jezza.oc.api.configuration.entries;

import me.jezza.oc.api.configuration.Config.ConfigDoubleArray;
import net.minecraftforge.common.config.Configuration;

import java.lang.reflect.Field;

public class ConfigEntryDoubleArray extends ConfigEntry<ConfigDoubleArray> {
    @Override
    public void processAnnotation(Configuration config, Field field, ConfigDoubleArray annotation) throws IllegalAccessException {
        double[] values = config.get(annotation.category(), field.getName(), annotation.defaultArray(), annotation.comment(), annotation.minValue(), annotation.maxValue(), annotation.isListLengthFixed(), annotation.maxListLength()).getDoubleList();
        field.set(null, values);
    }
}
