package me.jezza.oc.api.configuration;

import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.discovery.ASMDataTable;
import cpw.mods.fml.common.discovery.ASMDataTable.ASMData;
import cpw.mods.fml.common.discovery.ModCandidate;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import me.jezza.oc.api.configuration.discovery.ConfigData;
import me.jezza.oc.api.configuration.entries.*;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Set;

import static me.jezza.oc.api.configuration.Config.*;

public class ConfigHandler {

    // Once this is true, no more registering annotations.
    private static boolean processed = false;

    private static boolean init = false;

    private static final LinkedHashMap<Class<? extends Annotation>, Class<? extends ConfigEntry<? extends Annotation, ?>>> annotationMap;

    static {
        annotationMap = new LinkedHashMap<>();
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

        LinkedHashMap<ModContainer, ConfigData> configMap = new LinkedHashMap<>();
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
        ConfigData[] configValues = values.toArray(new ConfigData[values.size()]);

        // Process all potential registrars first.
        for (int i = 0; i < configValues.length; i++) {
            ConfigData configValue = configValues[i];
            if (configValue.isRegistrar)
                configValue.processIConfigRegistrar();
        }
        processed = true;

        // Organise all sub-packages.
        for (int i = 0; i < configValues.length; i++)
            configValues[i].processAllRoots();

        // Process all current classes associated with the ConfigContainer.
        for (int i = 0; i < configValues.length; i++)
            configValues[i].processConfigContainers(asmDataTable, annotationMap);
    }

    /**
     * To use this, implement {@link me.jezza.oc.api.configuration.Config.IConfigRegistrar} on your main mod file.
     * Only call this when you've had the chance from the interface.
     */
    public static boolean registerAnnotation(final Class<? extends Annotation> clazz, final Class<? extends ConfigEntry<? extends Annotation, ?>> configEntry) {
        if (processed)
            return false;
        if (!annotationMap.containsKey(clazz))
            annotationMap.put(clazz, configEntry);
        return true;
    }

    /**
     * Don't use this unless you know what you're doing...
     */
    public static boolean registerAnnotationOverride(final Class<? extends Annotation> clazz, final Class<? extends ConfigEntry<? extends Annotation, ?>> configEntry) {
        if (processed)
            return false;
        annotationMap.put(clazz, configEntry);
        return true;
    }

    @Override
    public String toString() {
        return annotationMap.toString();
    }
}
