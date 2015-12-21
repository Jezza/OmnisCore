package me.jezza.oc.common.core.config.discovery;

import com.google.common.base.Throwables;
import cpw.mods.fml.common.discovery.ASMDataTable.ASMData;
import me.jezza.oc.OmnisCore;
import me.jezza.oc.common.core.config.ConfigEntry;
import me.jezza.oc.common.core.config.ICEFactory;
import me.jezza.oc.common.core.config.OmnisConfiguration;
import me.jezza.oc.common.interfaces.InputBuffer;
import me.jezza.oc.common.interfaces.OutputBuffer;
import me.jezza.oc.common.utils.ASM;
import net.minecraft.entity.player.EntityPlayer;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * A config container for all config annotations under a specified package (Includes all sub-packages).
 * Controlled by a ConfigData instance for a ModContainer.
 */
public class ConfigContainer {
	private final OmnisConfiguration config;

	private Map<Class<? extends Annotation>, ConfigEntry<? extends Annotation, ?>> annotationMap = new LinkedHashMap<>();
	private Collection<String> childClasses;

	public ConfigContainer(File config) {
		this.config = new OmnisConfiguration(config);
	}

	public void childClasses(Collection<String> childClasses) {
		this.childClasses = childClasses;
	}

	@SuppressWarnings("unchecked")
	public <A extends Annotation, C extends ConfigEntry<A, ?>> ConfigContainer processAllClasses(Collection<ICEFactory<A, C>> entries) {
		for (ICEFactory<A, C> factory : entries) {
			Class<A> annotationClass = factory.annotationClass();
			for (Entry<ASMData, Field> entry : ASM.fieldsWith(annotationClass).entrySet()) {
				ASMData data = entry.getKey();
				if (!childClasses.contains(data.getClassName()))
					continue;
				Field field = entry.getValue();
				int mods = field.getModifiers();
				if (!Modifier.isStatic(mods)) {
					OmnisCore.logger.warn("Found @{} on a non-static field: {}.{}. Skipping...", annotationClass.getSimpleName(), field.getDeclaringClass(), field.getName());
					continue;
				}
				if (Modifier.isFinal(mods)) {
					OmnisCore.logger.warn("Found @{} on a final field: {}.{}. Skipping...", annotationClass.getSimpleName(), field.getDeclaringClass(), field.getName());
					continue;
				}
				field.setAccessible(true);
				C configEntry = (C) annotationMap.get(annotationClass);
				if (configEntry == null) {
					try {
						configEntry = factory.create(config);
						annotationMap.put(annotationClass, configEntry);
					} catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
						OmnisCore.logger.fatal("Failed to create instance of the registered ConfigEntry!", e);
						throw Throwables.propagate(e);
					}
				}
				configEntry.add(field, field.getAnnotation(annotationClass));
			}
		}
		return this;
	}

	public void operate(boolean saveFlag) {
		for (ConfigEntry<? extends Annotation, ?> configEntry : annotationMap.values())
			configEntry.processFields(saveFlag);
	}

	public void writeSync(EntityPlayer player, OutputBuffer buffer) {
		for (ConfigEntry<? extends Annotation, ?> configEntry : annotationMap.values())
			configEntry.writeSync(player, buffer);
	}

	public void readSync(InputBuffer buffer) {
		for (ConfigEntry<? extends Annotation, ?> configEntry : annotationMap.values())
			configEntry.readSync(buffer);
	}
}
