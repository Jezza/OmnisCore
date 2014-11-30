package me.jezza.oc.api.configuration;

import me.jezza.oc.common.core.CoreProperties;
import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.Level;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class ConfigEntry<T extends Annotation, D> {

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

    @SuppressWarnings("unchecked")
    public void processCurrentEntries(Configuration config) {
        for (Map.Entry<Field, T> entry : configMap.entrySet()) {
            Field field = entry.getKey();
            T value = entry.getValue();
            try {
                field.setAccessible(true);
                Object object = processAnnotation(config, field.getName().toLowerCase(), value, (D) field.get(null));
                field.set(null, object);
            } catch (Exception e) {
                CoreProperties.logger.log(Level.FATAL, String.format("Failed to configure field: %s, with annotated type: %s", field.getName(), value.annotationType().getSimpleName()));
                e.printStackTrace();
            }
        }
    }

    public abstract Object processAnnotation(Configuration config, String fieldName, T annotation, D defaultValue);

}
