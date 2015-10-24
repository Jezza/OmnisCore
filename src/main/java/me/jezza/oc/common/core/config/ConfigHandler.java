package me.jezza.oc.common.core.config;

import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.discovery.ASMDataTable.ASMData;
import me.jezza.oc.api.config.IConfigRegistry;
import me.jezza.oc.api.exceptions.ConfigurationException;
import me.jezza.oc.common.core.config.discovery.ConfigData;
import me.jezza.oc.common.core.config.entries.*;
import me.jezza.oc.common.utils.ASM;
import net.minecraftforge.common.config.Configuration;
import org.apache.commons.lang3.ClassUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import static me.jezza.oc.api.config.Config.*;

public final class ConfigHandler implements IConfigRegistry {
	private static ConfigHandler INSTANCE;

	// After 3rd party mod annotations have been added.
	private static boolean registrarsProcessed = false;
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

	static {
		internalRegister(ConfigBoolean.class, CEBoolean.class);
		internalRegister(ConfigBooleanArray.class, CEBooleanArray.class);
		internalRegister(ConfigInteger.class, CEInteger.class);
		internalRegister(ConfigIntegerArray.class, CEIntegerArray.class);
		internalRegister(ConfigFloat.class, CEFloat.class);
		internalRegister(ConfigDouble.class, CEDouble.class);
		internalRegister(ConfigDoubleArray.class, CEDoubleArray.class);
		internalRegister(ConfigString.class, CEString.class);
		internalRegister(ConfigStringArray.class, CEStringArray.class);
		internalRegister(ConfigEnum.class, CEEnum.class);
	}

	private ConfigHandler() {
	}

	private ConfigHandler parseControllers() {
		Set<ASMData> dataSet = ASM.dataTable(Controller.class);

		// Filters out all controller annotations and places them with their associated mod.
		for (ASMData data : dataSet) {
			final String packageName = ClassUtils.getPackageName(data.getClassName());
			String modId = ASM.findOwner(packageName).getModId();
			ConfigData configData = configMap.get(modId);
			if (configData == null) {
				Set<String> classes = ASM.ownedClasses(packageName);
				configData = new ConfigData(modId, classes);
				configMap.put(modId, configData);
			}
			configData.addRoot(data);
		}

		// Process all potential registrars first.
		for (Entry<ASMData, Method> entry : ASM.methodsWithExact(ConfigRegistrar.class, IConfigRegistry.class).entrySet()) {
			Method method = entry.getValue();
			if (!Modifier.isStatic(method.getModifiers()))
				throw new IllegalStateException("Found a @" + ConfigRegistrar.class + " on a non-static method!");
			Class<?>[] types = method.getParameterTypes();
			if (types.length != 1)
				throw new IllegalStateException("Found a @" + ConfigRegistrar.class + " on a method with an incorrect parameter count! Expected 1, found " + types.length);
			if (types[0] != IConfigRegistry.class)
				throw new IllegalStateException("Found a @" + ConfigRegistrar.class + " on a method with an incorrect parameter! Expected parameter of type IConfigRegistry, found " + types[0]);
			try {
				method.invoke(null, this);
			} catch (IllegalAccessException | InvocationTargetException e) {
				// Shouldn't happen.
				throw Throwables.propagate(e);
			}
		}
		registrarsProcessed = true;

		for (ConfigData configData : configMap.values()) {
			// Organise all sub-packages.
			configData.processRoots();

			// Process all current classes associated with the ConfigContainer.
			configData.processConfigContainers(annotationMap.values());
		}
		postProcessed = true;
		return this;
	}

	@Override
	public <A extends Annotation, T extends ConfigEntry<A, ?>> boolean registerAnnotation(Class<A> clazz, Class<T> configEntry) {
		return !(registrarsProcessed || Modifier.isAbstract(configEntry.getModifiers())) && internalRegister(clazz, configEntry);
	}

	private static <A extends Annotation, T extends ConfigEntry<A, ?>> boolean internalRegister(Class<A> clazz, Class<T> configEntry) {
		String canonicalName = clazz.getCanonicalName();
		if (!annotationMap.containsKey(canonicalName)) {
			annotationMap.put(canonicalName, createFactory(clazz, configEntry));
			return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	private static <A extends Annotation, T extends ConfigEntry<A, ?>> ICEFactory<A, T> createFactory(final Class<A> annotationClazz, final Class<T> configClazz) {
		for (final Constructor<T> constructor : (Constructor<T>[]) configClazz.getDeclaredConstructors()) {
			if (constructor.getParameterCount() == 1 && constructor.getParameterTypes()[0] == Configuration.class) {
				constructor.setAccessible(true);
				return new ICEFactory<A, T>() {
					@Override
					public T create(Object... params) throws InstantiationException, IllegalAccessException, InvocationTargetException {
						return constructor.newInstance(params);
					}

					@Override
					public Class<A> annotationClazz() {
						return annotationClazz;
					}
				};
			}
		}
		throw new ConfigurationException("Found no entry point for the ConfigClass: " + configClazz + "! Requires a constructor with one parameter: " + Configuration.class);
	}

	public static boolean load(String modID) {
		if (postProcessed && !Strings.isNullOrEmpty(modID) && Loader.isModLoaded(modID)) {
			ConfigData configData = configMap.get(modID);
			if (configData != null) {
				configData.load();
				return true;
			}
		}
		return false;
	}

	public static boolean save(String modID) {
		if (postProcessed && !Strings.isNullOrEmpty(modID) && Loader.isModLoaded(modID)) {
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
