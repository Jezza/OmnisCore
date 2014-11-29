package me.jezza.oc.api.configuration.entries;

import me.jezza.oc.api.configuration.Config.ConfigBooleanArray;
import net.minecraftforge.common.config.Configuration;

import java.lang.reflect.Field;

public class ConfigEntryBooleanArray extends ConfigEntry<ConfigBooleanArray> {
    @Override
    public void processAnnotation(Configuration config, Field field, ConfigBooleanArray annotation) throws IllegalAccessException {
        boolean[] values = config.get(annotation.category(), field.getName(), annotation.defaultArray(), annotation.comment(), annotation.isListLengthFixed(), annotation.maxListLength()).getBooleanList();
        field.set(null, values);
    }
}
