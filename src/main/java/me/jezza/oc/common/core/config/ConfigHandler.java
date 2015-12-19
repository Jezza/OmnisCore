package me.jezza.oc.common.core.config;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.discovery.ASMDataTable.ASMData;
import me.jezza.oc.common.core.config.discovery.ConfigData;
import me.jezza.oc.common.utils.ASM;
import me.jezza.oc.common.utils.helpers.StringHelper;
import net.minecraftforge.common.config.Configuration;
import org.apache.commons.lang3.ClassUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import static me.jezza.oc.common.core.config.Config.ConfigAnnotation;
import static me.jezza.oc.common.core.config.Config.Controller;
import static me.jezza.oc.common.utils.helpers.StringHelper.format;

public final class ConfigHandler {
	private static ConfigHandler INSTANCE;

	// After 3rd party mod annotations have been added.
	private static boolean annotationsRegistered = false;
	// After all annotated fields have been located, processed, and cached.
	private static boolean postProcessed = false;

	private static final Map<String, ConfigData> configMap = new LinkedHashMap<>();
	private static final Map<String, ICEFactory<?, ? extends ConfigEntry<? extends Annotation, ?>>> annotationMap = new LinkedHashMap<>();

	public static void init() {
		if (INSTANCE != null)
			return;
		INSTANCE = new ConfigHandler();
		INSTANCE.parseControllers();
	}

//	static {
//		internalRegister(ConfigBoolean.class, CEBoolean.class);
//		internalRegister(ConfigBooleanArray.class, CEBooleanArray.class);
//		internalRegister(ConfigInteger.class, CEInteger.class);
//		internalRegister(ConfigIntegerArray.class, CEIntegerArray.class);
//		internalRegister(ConfigFloat.class, CEFloat.class);
//		internalRegister(ConfigDouble.class, CEDouble.class);
//		internalRegister(ConfigDoubleArray.class, CEDoubleArray.class);
//		internalRegister(ConfigString.class, CEString.class);
//		internalRegister(ConfigStringArray.class, CEStringArray.class);
//		internalRegister(ConfigEnum.class, CEEnum.class);
//	}

	private ConfigHandler() {
	}

	private void parseControllers() {
		Set<ASMData> dataSet = ASM.dataTable(Controller.class);

		// Maps all the controller annotations to their associated mod.
		for (ASMData data : dataSet) {
			final String packageName = ClassUtils.getPackageName(data.getClassName());
			String modId = ASM.findOwner(packageName).getModId();
			ConfigData configData = configMap.get(modId);
			if (configData == null) {
				configData = new ConfigData(modId, ASM.ownedClasses(packageName));
				configMap.put(modId, configData);
			}
			configData.addRoot(data);
		}

		// Process all config annotations.
		for (Entry<ASMData, Class<?>> entry : ASM.classesWith(ConfigAnnotation.class).entrySet()) {
			@SuppressWarnings("unchecked")
			Class<? extends Annotation> value = (Class<? extends Annotation>) entry.getValue();
			ConfigAnnotation annotation = value.getAnnotation(ConfigAnnotation.class);
			if (annotation == null)
				throw new IllegalStateException(format("Configuration was supplied an invalid configuration annotation. {}"));
			Class<? extends ConfigEntry<? extends Annotation, ?>> configEntry = annotation.value();
			if (!Modifier.isAbstract(configEntry.getModifiers()))
				internalRegister(value, configEntry);
		}
		annotationsRegistered = true;

		// Begin the processing.
		for (ConfigData configData : configMap.values()) {
			// Organise all sub-packages, and process all classes associated with the ConfigContainer.
			configData.processRoots().processConfigContainers(annotationMap.values());
		}
		postProcessed = true;
	}

	public static <A extends Annotation, T extends ConfigEntry<A, ?>> boolean registerAnnotation(Class<A> annotationClazz, Class<T> configEntry) {
		return !(annotationsRegistered || Modifier.isAbstract(configEntry.getModifiers())) && internalRegister(annotationClazz, configEntry);
	}

	private static boolean internalRegister(Class<? extends Annotation> annotationClass, Class<? extends ConfigEntry<? extends Annotation, ?>> configEntry) {
		String canonicalName = annotationClass.getCanonicalName();
		if (!annotationMap.containsKey(canonicalName)) {
			annotationMap.put(canonicalName, createFactory(annotationClass, configEntry));
			return true;
		}
		return false;
	}

	private static <A extends Annotation, T extends ConfigEntry<A, ?>> ICEFactory<A, T> createFactory(final Class<A> annotationClass, final Class<? extends ConfigEntry<? extends Annotation, ?>> configClass) {
		try {
			@SuppressWarnings("unchecked")
			final Constructor<T> constructor = (Constructor<T>) configClass.getDeclaredConstructor(Configuration.class);
			constructor.setAccessible(true);
			return new ICEFactory<A, T>() {
				@Override
				public T create(Object... params) throws InstantiationException, IllegalAccessException, InvocationTargetException {
					return constructor.newInstance(params);
				}

				@Override
				public Class<A> annotationClass() {
					return annotationClass;
				}
			};
		} catch (NoSuchMethodException e) {
			throw new ConfigurationException(format("Invalid constructor; Expected constructor with {} in {}.", Configuration.class.getCanonicalName(), configClass));
		}
	}

	public static boolean load(String modID) {
		if (postProcessed && !StringHelper.useable(modID) && Loader.isModLoaded(modID)) {
			ConfigData configData = configMap.get(modID);
			if (configData != null) {
				configData.load();
				return true;
			}
		}
		return false;
	}

	public static boolean save(String modID) {
		if (postProcessed && StringHelper.useable(modID) && Loader.isModLoaded(modID)) {
			ConfigData configData = configMap.get(modID);
			if (configData != null) {
				configData.save();
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return annotationMap.toString();
	}
}
