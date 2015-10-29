package me.jezza.oc.common.utils;

import com.google.common.base.Predicate;
import com.google.common.base.Splitter;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ImmutableListMultimap.Builder;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.discovery.ASMDataTable;
import cpw.mods.fml.common.discovery.ASMDataTable.ASMData;
import cpw.mods.fml.common.discovery.ModCandidate;
import cpw.mods.fml.common.discovery.ModDiscoverer;
import cpw.mods.fml.relauncher.ReflectionHelper;
import me.jezza.oc.common.utils.helpers.Collections3;
import me.jezza.oc.common.utils.helpers.ModHelper;
import org.apache.commons.lang3.ClassUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

import static me.jezza.oc.common.utils.helpers.PredicateHelper.startsWith;

/**
 * @author Jezza
 */
public class ASM {
	private static final Splitter ARG_SPLITTER = Splitter.on(";").omitEmptyStrings().trimResults();

	private static final Map<String, Set<String>> ownedPackages = Maps.newHashMap();
	private static final Map<String, Set<String>> subClasses = Maps.newHashMap();

	private static ListMultimap<String, ModContainer> packageOwners;
	private static ModDiscoverer discoverer;
	private static ASMDataTable dataTable;
	private static LoadController loadController;

	private ASM() {
		throw new IllegalStateException();
	}

	public static ModContainer findOwner(Object object) {
		return findOwner(ClassUtils.getPackageName(object.getClass()));
	}

	public static ModContainer findOwner(Class<?> clazz) {
		return findOwner(ClassUtils.getPackageName(clazz));
	}

	public static ModContainer findOwner(String packageName) {
		List<ModContainer> containers = packageOwners().get(packageName);
		return containers != null ? containers.get(0) : Loader.instance().getMinecraftModContainer();
	}

	public static Set<String> ownedClasses(String packageName) {
		Set<String> classes = subClasses.get(packageName);
		if (classes == null) {
			Set<String> owned = Sets.newHashSet();
			Predicate<String> startsWith = startsWith(packageName);
			for (ModCandidate candidate : dataTable().getCandidatesFor(packageName))
				Collections3.addAll(owned, candidate.getClassList(), startsWith);
			classes = Collections.unmodifiableSet(owned);
			subClasses.put(packageName, classes);
		}
		return classes;
	}

	public static Set<String> ownedPackages(ModContainer container) {
		String modId = container.getModId();
		Set<String> packages = ownedPackages.get(modId);
		if (packages == null) {
			Object mod = container.getMod();
			List<String> ownedPackages = container.getOwnedPackages();
			if (mod == null) {
				packages = Collections.unmodifiableSet(new HashSet<>(ownedPackages));
			} else {
				final String packageName = ClassUtils.getPackageName(mod.getClass());
				Set<String> packageSet = Sets.newHashSetWithExpectedSize(ownedPackages.size());
				Collections3.filter(ownedPackages, packageSet, startsWith(packageName));
				packages = Collections.unmodifiableSet(packageSet);
			}
			ASM.ownedPackages.put(modId, packages);
		}
		return packages;
	}

	public static ListMultimap<String, ModContainer> packageOwners() {
		if (packageOwners == null) {
			Builder<String, ModContainer> builder = ImmutableListMultimap.builder();
			for (ModContainer container : ModHelper.getIndexedModMap().values())
				for (String ownedPackage : ownedPackages(container))
					builder.put(ownedPackage, container);
			packageOwners = builder.build();
		}
		return packageOwners;
	}

	public static LoadController loadController() {
		if (loadController == null)
			loadController = ReflectionHelper.getPrivateValue(Loader.class, Loader.instance(), "modController");
		return loadController;
	}

	public static ModContainer overrideActiveContainer(ModContainer container) {
		ModContainer oldContainer = ReflectionHelper.getPrivateValue(LoadController.class, loadController(), "activeContainer");
		ReflectionHelper.setPrivateValue(LoadController.class, loadController(), container, "activeContainer");
		return oldContainer;
	}

	public static ModDiscoverer discoverer() {
		if (discoverer == null)
			discoverer = ReflectionHelper.getPrivateValue(Loader.class, Loader.instance(), "discoverer");
		return discoverer;
	}

	public static ASMDataTable dataTable() {
		if (dataTable == null)
			dataTable = discoverer().getASMTable();
		return dataTable;
	}

	public static Set<ASMData> dataTable(Class<? extends Annotation> annotationClass) {
		return dataTable().getAll(annotationClass.getName());
	}

	public static Map<ASMData, Class<?>> classesWith(Class<? extends Annotation> annotationClass) {
		Map<ASMData, Class<?>> classes = Maps.newHashMap();
		for (ASMData data : dataTable(annotationClass))
			classes.put(data, getClass(data));
		return classes;
	}

	public static Map<ASMData, Field> fieldsWith(Class<? extends Annotation> annotationClass) {
		Map<ASMData, Field> fields = Maps.newHashMap();
		for (ASMData data : dataTable(annotationClass))
			fields.put(data, getField(data));
		return fields;
	}

	public static Map<ASMData, Method> methodsWith(Class<? extends Annotation> annotationClass) {
		Map<ASMData, Method> methods = Maps.newHashMap();
		for (ASMData data : dataTable(annotationClass))
			methods.put(data, getMethod(data));
		return methods;
	}

	public static Map<ASMData, Method> methodsWithExact(Class<? extends Annotation> annotationClass, Class<?>... parameterTypes) {
		Map<ASMData, Method> methods = Maps.newHashMap();
		for (ASMData data : dataTable(annotationClass))
			methods.put(data, getMethodWithExact(data, parameterTypes));
		return methods;
	}

	public static Class<?> loadClass(String name) {
		try {
			return Class.forName(name, true, Loader.instance().getModClassLoader());
		} catch (ClassNotFoundException e) {
			throw Throwables.propagate(e);
		}
	}

	public static Class<?> getClass(ASMData data) {
		return loadClass(data.getClassName());
	}

	public static Field getField(ASMData data) {
		try {
			Field field = loadClass(data.getClassName()).getDeclaredField(data.getObjectName());
			field.setAccessible(true);
			return field;
		} catch (NoSuchFieldException e) {
			throw Throwables.propagate(e);
		}
	}

	public static Method getMethod(ASMData data) {
		String objectName = data.getObjectName();
		int firstPar = objectName.indexOf('(');
		int secondPar = objectName.indexOf(')');
		String methodName = objectName.substring(0, firstPar);
		List<String> args = ARG_SPLITTER.splitToList(objectName.substring(firstPar + 1, secondPar));
		// String returnType = objectName.substring(secondPar + 1);
		Class<?> clazz = loadClass(data.getClassName());
		for (Method method : clazz.getDeclaredMethods()) {
			if (methodName.equals(method.getName())) {
				Class<?>[] parameters = method.getParameterTypes();
				if (parameters.length == args.size()) {
					if (parameters.length == 0) {
						method.setAccessible(true);
						return method;
					}
					for (int i = 0; i < args.size(); i++) {
						if (args.get(i).replace("/", ".").substring(1).equals(parameters[i].getCanonicalName())) {
							method.setAccessible(true);
							return method;
						}
					}
				}
			}
		}
		throw new RuntimeException(new NoSuchMethodException(data.getClassName() + '.' + objectName));
	}

	public static Method getMethodWithExact(ASMData data, Class<?>... parameterTypes) {
		try {
			String objectName = data.getObjectName();
			Method method = loadClass(data.getClassName()).getDeclaredMethod(objectName.substring(0, objectName.indexOf('(')), parameterTypes);
			method.setAccessible(true);
			return method;
		} catch (NoSuchMethodException e) {
			throw Throwables.propagate(e);
		}
	}
}
