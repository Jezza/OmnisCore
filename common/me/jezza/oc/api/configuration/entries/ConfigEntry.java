package me.jezza.oc.api.configuration.entries;

import me.jezza.oc.common.core.CoreProperties;
import net.minecraftforge.common.config.Configuration;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class ConfigEntry<T extends Annotation> {

    private LinkedHashMap<Field, T> configMap;

    public ConfigEntry() {
        configMap = new LinkedHashMap<>();
    }

    public boolean containsField(Field field) {
        return configMap.containsKey(field);
    }

    public void add(Field field, T annotation) {
        if (!containsField(field))
            configMap.put(field, annotation);
    }

    public void processCurrentEntries(Configuration config) {
        for (Map.Entry<Field, T> entry : configMap.entrySet()) {
            Field field = entry.getKey();
            T value = entry.getValue();
            try {
                processAnnotation(config, field, value);
            } catch (IllegalAccessException e) {
                CoreProperties.logger.fatal(createErrorMessage(field, value.annotationType()));
                e.printStackTrace();
            }
        }
    }

    public abstract void processAnnotation(Configuration config, Field field, T annotation) throws IllegalAccessException;

    public String createErrorMessage(Field field, Class<? extends Annotation> annotationType) {
        return "Failed to configure field: " + field.getName() + ", with annotated type: " + annotationType.getSimpleName();
    }

}
