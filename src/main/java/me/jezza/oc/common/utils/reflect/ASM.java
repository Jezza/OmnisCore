package me.jezza.oc.common.utils.reflect;

import static me.jezza.oc.common.utils.helpers.PredicateHelper.startsWith;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import com.google.common.base.Predicate;
import com.google.common.base.Splitter;
import com.google.common.base.Throwables;
import com.google.common.collect.*;
import com.google.common.collect.ImmutableListMultimap.Builder;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.LoaderState;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.discovery.ASMDataTable;
import cpw.mods.fml.common.discovery.ASMDataTable.ASMData;
import cpw.mods.fml.common.discovery.ModCandidate;
import cpw.mods.fml.common.discovery.ModDiscoverer;
import cpw.mods.fml.relauncher.ReflectionHelper;
import me.jezza.oc.OmnisCore;
import me.jezza.oc.common.utils.Mods;
import me.jezza.oc.common.utils.collect.Collections3;
import me.jezza.oc.common.utils.helpers.StringHelper;
import org.apache.commons.lang3.ClassUtils;

/**
 * @author Jezza
 */
public class ASM {

	private static final Splitter ARG_SPLITTER = Splitter.on(";").omitEmptyStrings().trimResults();

	private static final Map<String, Set<String>> ownedPackages = Maps.newHashMap();
	private static final Map<String, Set<String>> subClasses = Maps.newHashMap();

	private static final Map<Class<? extends Annotation>, Map<ASMData, Class<?>>> classCache = Maps.newHashMap();
	private static final Map<Class<? extends Annotation>, Map<ASMData, Field>> fieldCache = Maps.newHashMap();
	private static final Map<Class<? extends Annotation>, Map<ASMData, Method>> methodCache = Maps.newHashMap();

	private static final int CALLING_DEPTH = 5;

	private static ListMultimap<String, ModContainer> packageOwners;
	private static ModDiscoverer discoverer;
	private static ASMDataTable dataTable;
	private static LoadController loadController;
	private static TypedField<ModContainer> acf;
	private static ModContainer minecraft;
	private static Method callingStack;

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
		return containers != null ? containers.get(0) : minecraft();
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

	public static ListMultimap<String, ModContainer> packageOwners() {
		if (packageOwners == null) {
			Builder<String, ModContainer> builder = ImmutableListMultimap.builder();
			for (ModContainer container : Mods.map().values())
				for (String ownedPackage : ownedPackages(container))
					builder.put(ownedPackage, container);
			packageOwners = builder.build();
		}
		return packageOwners;
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

	public static LoadController loadController() {
		if (loadController == null)
			loadController = ReflectionHelper.getPrivateValue(Loader.class, Loader.instance(), "modController");
		return loadController;
	}

	public static boolean isInState(LoaderState state) {
		return Loader.instance().isInState(state);
	}

	public static boolean hasReachedState(LoaderState state) {
		return Loader.instance().hasReachedState(state);
	}

	private static TypedField<ModContainer> acf() {
		if (acf == null)
			acf = TypedField.find(LoadController.class, ModContainer.class, "activeContainer");
		return acf;
	}

	public static ModContainer overrideAC(ModContainer container) {
		try {
			ModContainer oldContainer = acf().get(loadController());
			acf().set(loadController(), container);
			return oldContainer;
		} catch (IllegalAccessException e) {
			throw Throwables.propagate(e);
		}
	}

	public static void runWithContainer(ModContainer container, Runnable runnable) {
		try {
			ModContainer oldContainer = acf().get(loadController());
			acf().set(loadController(), container);
			runnable.run();
			acf().set(loadController(), oldContainer);
		} catch (IllegalAccessException e) {
			throw Throwables.propagate(e);
		}
	}

	public static Class<?>[] callingStack() {
		if (callingStack == null) {
			try {
				callingStack = LoadController.class.getDeclaredMethod("getCallingStack");
				callingStack.setAccessible(true);
			} catch (NoSuchMethodException e) {
				throw Throwables.propagate(e);
			}
		}
		try {
			return (Class<?>[]) callingStack.invoke(loadController());
		} catch (IllegalAccessException | InvocationTargetException e) {
			throw Throwables.propagate(e);
		}
	}

	public static Class<?> callingClass() {
		Class<?>[] classes = callingStack();
		return classes[Math.min(CALLING_DEPTH, classes.length - 1)];
	}

	public static ModContainer callingMod() {
		return findOwner(callingClass());
	}

	public static ModContainer minecraft() {
		if (minecraft == null)
			minecraft = Loader.instance().getMinecraftModContainer();
		return minecraft;
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
		Map<ASMData, Class<?>> classes = classCache.get(annotationClass);
		if (classes != null)
			return Maps.newHashMap(classes);
		ImmutableMap.Builder<ASMData, Class<?>> builder = ImmutableMap.builder();
		Set<String> discovered = new HashSet<>();
		for (ASMData data : dataTable(annotationClass)) {
			String className = data.getClassName();
			if (!discovered.contains(className) && className.equals(data.getObjectName())) {
				Class<?> dataClass = loadClass(className);
				builder.put(data, dataClass);
				discovered.add(className);
			}
		}
		ImmutableMap<ASMData, Class<?>> map = builder.build();
		classCache.put(annotationClass, map);
		return Maps.newHashMap(map);
	}

	public static Map<ASMData, Field> fieldsWith(Class<? extends Annotation> annotationClass) {
		Map<ASMData, Field> fields = fieldCache.get(annotationClass);
		if (fields != null)
			return Maps.newHashMap(fields);
		ImmutableMap.Builder<ASMData, Field> builder = ImmutableMap.builder();
		for (ASMData data : dataTable(annotationClass))
			builder.put(data, getField(data));
		ImmutableMap<ASMData, Field> map = builder.build();
		fieldCache.put(annotationClass, map);
		return Maps.newHashMap(map);
	}

	public static Map<ASMData, Method> methodsWith(Class<? extends Annotation> annotationClass) {
		Map<ASMData, Method> methods = methodCache.get(annotationClass);
		if (methods != null)
			return Maps.newHashMap(methods);
		ImmutableMap.Builder<ASMData, Method> builder = ImmutableMap.builder();
		for (ASMData data : dataTable(annotationClass))
			builder.put(data, getMethod(data));
		ImmutableMap<ASMData, Method> map = builder.build();
		methodCache.put(annotationClass, map);
		return Maps.newHashMap(map);
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
			OmnisCore.logger.fatal(StringHelper.format("Failed to load class: {}!", name), e);
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

	public static <T> TypedField<T> findTypedFind(Class<?> clazz, Class<T> type, String... fieldNames) {
		return TypedField.find(clazz, type, fieldNames);
	}
}
