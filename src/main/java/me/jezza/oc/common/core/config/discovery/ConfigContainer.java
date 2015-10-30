package me.jezza.oc.common.core.config.discovery;

import cpw.mods.fml.common.discovery.ASMDataTable;
import cpw.mods.fml.common.discovery.ASMDataTable.ASMData;
import me.jezza.oc.OmnisCore;
import me.jezza.oc.common.core.config.ConfigEntry;
import me.jezza.oc.common.core.config.ICEFactory;
import me.jezza.oc.common.utils.ASM;
import net.minecraftforge.common.config.Configuration;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A main config container for all the config annotations.
 * This includes all sub-packages.
 * Controlled by a ConfigData instance for a ModContainer.
 */
public class ConfigContainer {
	private Map<Class<? extends Annotation>, ConfigEntry<? extends Annotation, ?>> annotationMap = new LinkedHashMap<>();

	private Collection<String> childClasses;
	private Configuration config;

	public ConfigContainer(File config) {
		this.config = new Configuration(config);
	}

	public void setChildClasses(Collection<String> childClasses) {
		this.childClasses = childClasses;
	}

	public void processAllClasses(Collection<ICEFactory<?, ? extends ConfigEntry<? extends Annotation, ?>>> configEntryMap) {
		for (ICEFactory<?, ? extends ConfigEntry<? extends Annotation, ?>> factory : configEntryMap) {
			try {
				annotationMap.put(factory.annotationClass(), factory.create(config));
			} catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
				OmnisCore.logger.fatal("Failed to create instance of the registered ConfigEntry!", e);
			}
		}

		ASMDataTable dataTable = ASM.dataTable();
		ArrayList<String> processedClasses = new ArrayList<>();
		for (Class<? extends Annotation> annotationClazz : annotationMap.keySet()) {
			for (ASMData asmData : dataTable.getAll(annotationClazz.getName())) {
				String className = asmData.getClassName();
				if (childClasses.contains(className) && !processedClasses.contains(className)) {
					processedClasses.add(className);
					try {
						processClass(Class.forName(className));
					} catch (ClassNotFoundException e) {
						// Should never happen seeing as we got it from the system.
						OmnisCore.logger.fatal("Failed to find class!", e);
					}
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void processClass(Class<?> clazz) {
		for (final Field field : clazz.getDeclaredFields()) {
			for (Annotation annotation : field.getAnnotations()) {
				Class<? extends Annotation> annotationClazz = annotation.annotationType();
				if (annotationMap.containsKey(annotationClazz)) {
					int mods = field.getModifiers();
					if (!Modifier.isStatic(mods)) {
						OmnisCore.logger.warn("Found {} on a non-static field: {}.{}. Skipping...", annotationClazz.getCanonicalName(), field.getDeclaringClass(), field.getName());
						continue;
					}
					if (Modifier.isFinal(mods)) {
						OmnisCore.logger.warn("Found {} on a final field: {}.{}. Skipping...", annotationClazz.getCanonicalName(), field.getDeclaringClass(), field.getName());
						continue;
					}
					field.setAccessible(true);
					// Adds the field and the annotation to the ConfigEntry. No more annotations should be on the field, so break out of it and continue field iteration.
					((ConfigEntry<Annotation, Object>) annotationMap.get(annotationClazz)).add(field, annotation);
					break;
				}
			}
		}
	}

	public void operateOnConfig(boolean saveFlag) {
		config.load();
		for (ConfigEntry<? extends Annotation, ?> configEntry : annotationMap.values())
			configEntry.processFields(saveFlag);
		if (config.hasChanged())
			config.save();
	}
}
