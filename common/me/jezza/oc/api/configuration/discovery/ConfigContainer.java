package me.jezza.oc.api.configuration.discovery;

import cpw.mods.fml.common.discovery.ASMDataTable;
import cpw.mods.fml.common.discovery.ASMDataTable.ASMData;
import me.jezza.oc.api.configuration.ConfigEntry;
import me.jezza.oc.common.core.CoreProperties;
import net.minecraftforge.common.config.Configuration;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;

/**
 * A main config container for all the config annotations.
 * This includes all sub-packages.
 * These are all contained within a certain ModContainer.
 */
public class ConfigContainer {

    private Map<Class<? extends Annotation>, ConfigEntry<? extends Annotation, ?>> annotationMap;

    private String rootPackage;
    private Collection<String> childClasses;
    private Configuration config;
    private String configPath;

    public ConfigContainer(String rootPackage, File config) {
        this.config = new Configuration(config);
        this.configPath = config.getPath().replaceAll("\\\\", "/");
        annotationMap = new LinkedHashMap<>();
        this.rootPackage = rootPackage;
    }

    public Configuration getConfig() {
        return config;
    }

    public void setChildClasses(Collection<String> childClasses) {
        this.childClasses = childClasses;
    }

    public void processAllClasses(ASMDataTable dataTable, Map<Class<? extends Annotation>, Class<? extends ConfigEntry<? extends Annotation, ?>>> staticMap) {
        instantiateAnnotationMap(staticMap);

        ArrayList<String> processedClasses = new ArrayList<>();
        for (Class<? extends Annotation> clazz : annotationMap.keySet()) {
            Set<ASMData> data = dataTable.getAll(clazz.getName());
            for (ASMData asmData : data) {
                String className = asmData.getClassName();
                if (childClasses.contains(className) && !processedClasses.contains(className)) {
                    processedClasses.add(className);
                    processClass(className);
                }
            }
        }

        loadFromConfig();
    }

    private void instantiateAnnotationMap(Map<Class<? extends Annotation>, Class<? extends ConfigEntry<? extends Annotation, ?>>> staticMap) {
        for (Map.Entry<Class<? extends Annotation>, Class<? extends ConfigEntry<? extends Annotation, ?>>> entry : staticMap.entrySet()) {
            try {
                annotationMap.put(entry.getKey(), entry.getValue().newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                CoreProperties.logger.fatal("Failed to create instance for ConfigEntry!", e);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void processClass(String className) {
        Class<?> clazz;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            CoreProperties.logger.fatal("Failed to find class!", e);
            return;
        }

        for (final Field field : clazz.getDeclaredFields())
            for (Class<? extends Annotation> annotationClazz : annotationMap.keySet())
                if (field.isAnnotationPresent(annotationClazz)) {
                    ((ConfigEntry<Annotation, Object>) annotationMap.get(annotationClazz)).add(field, annotationClazz.cast(field.getAnnotation(annotationClazz)));
                    break;
                }
    }

    public void loadFromConfig() {
        config.load();
        for (ConfigEntry<? extends Annotation, ?> configEntry : annotationMap.values())
            configEntry.processCurrentEntries(config);
        config.save();
    }

    public String getRootPackage() {
        return rootPackage;
    }

    public String getConfigPath() {
        return configPath;
    }

}
