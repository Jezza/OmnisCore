package me.jezza.oc.api.configuration.entries;

import me.jezza.oc.api.configuration.Config.ConfigString;
import net.minecraftforge.common.config.Configuration;

import java.lang.reflect.Field;

public class ConfigEntryString extends ConfigEntry<ConfigString> {
    @Override
    public void processAnnotation(Configuration config, Field field, ConfigString annotation) throws IllegalAccessException {
        String value = config.getString(field.getName(), annotation.category(), annotation.defaultValue(), annotation.comment(), annotation.validValues());
        field.set(null, value);
    }
}
