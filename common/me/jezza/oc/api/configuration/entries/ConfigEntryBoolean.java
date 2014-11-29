package me.jezza.oc.api.configuration.entries;

import me.jezza.oc.api.configuration.Config.ConfigBoolean;
import net.minecraftforge.common.config.Configuration;

import java.lang.reflect.Field;

public class ConfigEntryBoolean extends ConfigEntry<ConfigBoolean> {
    @Override
    public void processAnnotation(Configuration config, Field field, ConfigBoolean annotation) throws IllegalAccessException {
        boolean value = config.getBoolean(field.getName(), annotation.category(), annotation.defaultValue(), annotation.comment());
        field.setBoolean(null, value);
    }
}
