package me.jezza.oc.api.configuration;

import me.jezza.oc.api.configuration.entries.*;
import net.minecraftforge.common.config.Configuration;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;

import static me.jezza.oc.api.configuration.Config.*;

public class ConfigHandler {

    private final LinkedHashMap<Class<? extends Annotation>, ConfigEntry<? extends Annotation, ?>> configMap;

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

    public void registerAnnotation(final Class<? extends Annotation> clazz, final ConfigEntry<? extends Annotation, ?> configEntry) {
        if (!configMap.containsKey(clazz))
            configMap.put(clazz, configEntry);
    }

    @SuppressWarnings("unchecked")
    public void register(Class<?> clazz) {
        for (final Field field : clazz.getDeclaredFields())
            for (Class<? extends Annotation> annotationClazz : configMap.keySet())
                if (field.isAnnotationPresent(annotationClazz))
                    ((ConfigEntry<Annotation, Object>) configMap.get(annotationClazz)).add(field, annotationClazz.cast(field.getAnnotation(annotationClazz)));
    }

    public void readFrom(File file) {
        Configuration config = new Configuration(file);
        config.load();
        processAllCurrentAnnotations(config);
        config.save();
    }

    public void processAllCurrentAnnotations(Configuration config) {
        for (ConfigEntry<? extends Annotation, ?> configEntry : configMap.values())
            configEntry.processCurrentEntries(config);
    }

    @Override
    public String toString() {
        return configMap.toString();
    }
}
