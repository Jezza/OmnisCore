package me.jezza.oc.api.configuration;

import me.jezza.oc.api.configuration.entries.*;
import net.minecraftforge.common.config.Configuration;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;

import static me.jezza.oc.api.configuration.Config.*;

public class ConfigHandler {

    private final LinkedHashMap<Class<? extends Annotation>, ConfigEntry<? extends Annotation>> configMap;

    {
        configMap = new LinkedHashMap<>();
        configMap.put(ConfigBoolean.class, new ConfigEntryBoolean());
        configMap.put(ConfigBooleanArray.class, new ConfigEntryBooleanArray());
        configMap.put(ConfigInteger.class, new ConfigEntryInteger());
        configMap.put(ConfigIntegerArray.class, new ConfigEntryIntegerArray());
        configMap.put(ConfigFloat.class, new ConfigEntryFloat());
        configMap.put(ConfigDouble.class, new ConfigEntryDouble());
        configMap.put(ConfigDoubleArray.class, new ConfigEntryDoubleArray());
        configMap.put(ConfigString.class, new ConfigEntryBoolean());
        configMap.put(ConfigStringArray.class, new ConfigEntryBoolean());
    }

    public ConfigHandler() {
    }

    public void registerAnnotation(final Class<? extends Annotation> clazz, final ConfigEntry<? extends Annotation> configEntry) {
        configMap.put(clazz, configEntry);
    }

    public void register(Class<?> clazz) {
        for (Field field : clazz.getDeclaredFields())
            processAnnotations(field);
    }

    @SuppressWarnings("unchecked")
    private void processAnnotations(final Field field) {
        for (Class<? extends Annotation> clazz : configMap.keySet())
            if (field.isAnnotationPresent(clazz))
                ((ConfigEntry<Annotation>) configMap.get(clazz)).add(field, clazz.cast(field.getAnnotation(clazz)));
    }

    public void readFrom(File file) {
        Configuration config = new Configuration(file);
        config.load();
        processAllCurrentAnnotations(config);
        config.save();
    }

    public void processAllCurrentAnnotations(Configuration config) {
        for (ConfigEntry<? extends Annotation> configEntry : configMap.values())
            configEntry.processCurrentEntries(config);
    }

    @Override
    public String toString() {
        return configMap.toString();
    }
}
