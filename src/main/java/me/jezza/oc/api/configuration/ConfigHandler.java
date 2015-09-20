package me.jezza.oc.api.configuration;

import com.google.common.base.Strings;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.discovery.ASMDataTable;
import cpw.mods.fml.common.discovery.ASMDataTable.ASMData;
import cpw.mods.fml.common.discovery.ModCandidate;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import me.jezza.oc.api.configuration.discovery.ConfigData;
import me.jezza.oc.api.configuration.entries.*;
import me.jezza.oc.api.configuration.exceptions.ConfigurationException;
import me.jezza.oc.api.configuration.lib.ICEFactory;
import me.jezza.oc.api.configuration.lib.IConfigRegistry;
import net.minecraftforge.common.config.Configuration;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import static me.jezza.oc.api.configuration.Config.*;

public class ConfigHandler implements IConfigRegistry {
    private static ConfigHandler INSTANCE;

    // After 3rd party mod annotations have been added.
    private static boolean registrarsProcessed = false;
    // After all annotated fields have been located, processed, and cached.
    private static boolean postProcessed = false;

    private static final Map<String, ConfigData> configMap = new LinkedHashMap<>();
    private static final Map<String, ICEFactory<?, ? extends ConfigEntry<? extends Annotation, ?>>> annotationMap = new LinkedHashMap<>();

    static {
        internalRegisterAnnotation(ConfigBoolean.class, CEBoolean.class);
        internalRegisterAnnotation(ConfigBooleanArray.class, CEBooleanArray.class);
        internalRegisterAnnotation(ConfigInteger.class, CEInteger.class);
        internalRegisterAnnotation(ConfigIntegerArray.class, CEIntegerArray.class);
        internalRegisterAnnotation(ConfigFloat.class, CEFloat.class);
        internalRegisterAnnotation(ConfigDouble.class, CEDouble.class);
        internalRegisterAnnotation(ConfigDoubleArray.class, CEDoubleArray.class);
        internalRegisterAnnotation(ConfigString.class, CEString.class);
        internalRegisterAnnotation(ConfigStringArray.class, CEStringArray.class);
        internalRegisterAnnotation(ConfigEnum.class, CEEnum.class);
    }

    public static void init(FMLPreInitializationEvent event) {
        if (INSTANCE != null)
            return;
        INSTANCE = new ConfigHandler();
        INSTANCE.parseControllers(event);
    }

    private ConfigHandler() {
    }

    public void parseControllers(FMLPreInitializationEvent event) {
        ASMDataTable asmDataTable = event.getAsmData();
        Set<ASMData> asmDataSet = asmDataTable.getAll(Controller.class.getName());

        // Filters out all controller annotations and places them with their associated mod.
        for (ASMData data : asmDataSet) {
            ModCandidate candidate = data.getCandidate();
            for (ModContainer modContainer : candidate.getContainedMods()) {
                String modId = modContainer.getModId();
                if (!configMap.containsKey(modId))
                    configMap.put(modId, new ConfigData(modContainer, candidate.getClassList()));
                configMap.get(modId).addRoot(data);
            }
        }

        // Process all potential registrars first.
        for (ConfigData configValue : configMap.values())
            if (configValue.isRegistrar)
                configValue.processIConfigRegistrar(this);
        registrarsProcessed = true;

        for (ConfigData configData : configMap.values()) {
            // Organise all sub-packages.
            configData.processRoots();

            // Process all current classes associated with the ConfigContainer.
            configData.processConfigContainers(asmDataTable, annotationMap.values());
        }
        postProcessed = true;
    }

    /**
     * You can only call this when you've had the chance from the interface.
     * To use this, implement {@link me.jezza.oc.api.configuration.Config.IConfigRegistrar} on your main mod file.
     * Please make sure you use that interface, that way I can guarantee that all the annotations are registered before all processing begins.
     */
    @Override
    public <A extends Annotation, T extends ConfigEntry<A, ?>> boolean registerAnnotation(Class<A> clazz, Class<T> configEntry) {
        return !(registrarsProcessed || Modifier.isAbstract(configEntry.getModifiers())) && internalRegisterAnnotation(clazz, configEntry);
    }

    private static <A extends Annotation, T extends ConfigEntry<A, ?>> boolean internalRegisterAnnotation(Class<A> clazz, Class<T> configEntry) {
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

    public static void load(String modID) {
        if (postProcessed && !Strings.isNullOrEmpty(modID) && Loader.isModLoaded(modID)) {
            ConfigData configData = configMap.get(modID);
            if (configData != null)
                configData.load();
        }
    }

    public static void save(String modID) {
        if (postProcessed && !Strings.isNullOrEmpty(modID) && Loader.isModLoaded(modID)) {
            ConfigData configData = configMap.get(modID);
            if (configData != null)
                configData.save();
        }
    }

    @Override
    public String toString() {
        return annotationMap.toString();
    }
}
