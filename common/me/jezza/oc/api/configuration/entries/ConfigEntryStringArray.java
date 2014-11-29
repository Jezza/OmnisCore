package me.jezza.oc.api.configuration.entries;

import me.jezza.oc.api.configuration.Config.ConfigStringArray;
import net.minecraftforge.common.config.Configuration;

import java.lang.reflect.Field;

public class ConfigEntryStringArray extends ConfigEntry<ConfigStringArray> {
    @Override
    public void processAnnotation(Configuration config, Field field, ConfigStringArray annotation) throws IllegalAccessException {
        String[] values = config.getStringList(field.getName(), annotation.category(), annotation.defaultArray(), annotation.comment(), annotation.validValues());
        field.set(null, values);
    }
}
