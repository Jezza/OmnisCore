package me.jezza.oc.api.configuration.discovery;

import me.jezza.oc.api.configuration.ConfigEntry;
import me.jezza.oc.common.core.CoreProperties;
import net.minecraftforge.common.config.Configuration;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A main config container for all the config annotations.
 * This includes all sub-packages.
 */
public class ConfigContainer {

    private LinkedHashMap<Class<? extends Annotation>, ConfigEntry<? extends Annotation, ?>> annotationMap;

    private Collection<String> childClasses;
    private Configuration config;

    public ConfigContainer(File config) {
        annotationMap = new LinkedHashMap<>();
        this.config = new Configuration(config);
    }

    public void setChildClasses(Collection<String> childPackages) {
        this.childClasses = childPackages;
    }

    public void processAllClasses(LinkedHashMap<Class<? extends Annotation>, Class<? extends ConfigEntry<? extends Annotation, ?>>> staticMap) {
        for (Map.Entry<Class<? extends Annotation>, Class<? extends ConfigEntry<? extends Annotation, ?>>> entry : staticMap.entrySet()) {
            try {
                annotationMap.put(entry.getKey(), entry.getValue().newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                // TODO ERROR MESSAGES
                CoreProperties.logger.fatal("Failed to create instance for ConfigEntry!", e);
            }
        }

        for (String childClass : childClasses) {
            try {
                processClass(Class.forName(childClass));
            } catch (ClassNotFoundException e) {
                // TODO ERROR MESSAGES
                CoreProperties.logger.fatal("FAILED TO FIND CLASS!", e);
            }
        }
        loadFromConfig();
    }

    @SuppressWarnings("unchecked")
    private void processClass(Class<?> clazz) {
        for (final Field field : clazz.getDeclaredFields())
            for (Class<? extends Annotation> annotationClazz : annotationMap.keySet())
                if (field.isAnnotationPresent(annotationClazz))
                    ((ConfigEntry<Annotation, Object>) annotationMap.get(annotationClazz)).add(field, annotationClazz.cast(field.getAnnotation(annotationClazz)));
    }

    private void loadFromConfig() {
        config.load();
        for (ConfigEntry<? extends Annotation, ?> configEntry : annotationMap.values())
            configEntry.processCurrentEntries(config);
        config.save();
    }

}
