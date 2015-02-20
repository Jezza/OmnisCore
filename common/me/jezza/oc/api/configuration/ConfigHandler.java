package me.jezza.oc.api.configuration;

import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.discovery.ASMDataTable;
import cpw.mods.fml.common.discovery.ASMDataTable.ASMData;
import cpw.mods.fml.common.discovery.ModCandidate;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import me.jezza.oc.api.configuration.discovery.ConfigData;
import me.jezza.oc.api.configuration.entries.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Set;

import static me.jezza.oc.api.configuration.Config.*;

public class ConfigHandler {

    // Once this is true, no more registering annotations.
    private static boolean processed = false;
    private static boolean postProcessed = false;
    private static boolean init = false;

    private static final LinkedHashMap<ModContainer, ConfigData> configMap = new LinkedHashMap<>();

    private static final LinkedHashMap<Class<? extends Annotation>, Class<? extends ConfigEntry<? extends Annotation, ?>>> annotationMap = new LinkedHashMap<>();

    static {
        annotationMap.put(ConfigBoolean.class, ConfigEntryBoolean.class);
        annotationMap.put(ConfigBooleanArray.class, ConfigEntryBooleanArray.class);
        annotationMap.put(ConfigInteger.class, ConfigEntryInteger.class);
        annotationMap.put(ConfigIntegerArray.class, ConfigEntryIntegerArray.class);
        annotationMap.put(ConfigFloat.class, ConfigEntryFloat.class);
        annotationMap.put(ConfigDouble.class, ConfigEntryDouble.class);
        annotationMap.put(ConfigDoubleArray.class, ConfigEntryDoubleArray.class);
        annotationMap.put(ConfigString.class, ConfigEntryBoolean.class);
        annotationMap.put(ConfigStringArray.class, ConfigEntryBoolean.class);
    }

    private ConfigHandler() {
    }

    public static void initConfigHandler(FMLPreInitializationEvent event) {
        if (init)
            return;
        init = true;

        ASMDataTable asmDataTable = event.getAsmData();
        Set<ASMData> asmDataSet = asmDataTable.getAll(Controller.class.getName());

        // Filters out all annotations and places them with their associated mod.
        for (ASMData data : asmDataSet) {
            ModCandidate candidate = data.getCandidate();
            for (ModContainer modContainer : candidate.getContainedMods()) {
                if (!configMap.containsKey(modContainer))
                    configMap.put(modContainer, new ConfigData(modContainer, candidate.getClassList()));
                configMap.get(modContainer).addRoot(data);
            }
        }

        Collection<ConfigData> values = configMap.values();

        // Process all potential registrars first.
        for (ConfigData configValue : values)
            if (configValue.isRegistrar)
                configValue.processIConfigRegistrar();
        processed = true;

        for (ConfigData configData : values) {
            // Organise all sub-packages.
            configData.processAllRoots();

            // Process all current classes associated with the ConfigContainer.
            configData.processConfigContainers(asmDataTable, annotationMap);
        }
        postProcessed = true;
    }

    /**
     * Only call this when you've had the chance from the interface.
     * To use this, implement {@link me.jezza.oc.api.configuration.Config.IConfigRegistrar} on your main mod file.
     */
    public static boolean registerAnnotation(final Class<? extends Annotation> clazz, final Class<? extends ConfigEntry<? extends Annotation, ?>> configEntry) {
        if (processed)
            return false;
        if (Modifier.isAbstract(configEntry.getModifiers()))
            return false;
        if (!annotationMap.containsKey(clazz))
            annotationMap.put(clazz, configEntry);
        return true;
    }

    public static ConfigData getConfigData(ModContainer modContainer) {
        if (!postProcessed)
            return null;
        return configMap.get(modContainer);
    }

    @Override
    public String toString() {
        return annotationMap.toString();
    }
}
