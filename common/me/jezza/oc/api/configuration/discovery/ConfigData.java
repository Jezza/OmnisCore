package me.jezza.oc.api.configuration.discovery;

import com.google.common.base.Strings;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.discovery.ASMDataTable;
import cpw.mods.fml.common.discovery.ASMDataTable.ASMData;
import me.jezza.oc.api.configuration.Config.IConfigRegistrar;
import me.jezza.oc.api.configuration.ConfigEntry;
import me.jezza.oc.common.core.CoreProperties;

import java.io.File;
import java.lang.annotation.Annotation;
import java.util.*;

/**
 * The config data manager for any given ModContainer.
 */
public class ConfigData {
    private static final File CONFIG_DIR = new File(".", "config");

    private Collection<String> ownedClasses;
    TreeMap<String, ConfigContainer> configSet;
    private ModContainer modContainer;
    public final boolean isRegistrar;

    public ConfigData(ModContainer modContainer, Collection<String> ownedClasses) {
        this.modContainer = modContainer;
        copyOwnedClasses(ownedClasses);
        isRegistrar = modContainer.getMod() instanceof IConfigRegistrar;
        configSet = new TreeMap<>(dataComparator);
    }

    private void copyOwnedClasses(Collection<String> ownedClasses) {
        ArrayList<String> sortedClasses = new ArrayList<>();
        for (String s : ownedClasses)
            if (!sortedClasses.contains(s))
                sortedClasses.add(s);
        this.ownedClasses = sortedClasses;
    }

    public void addRoot(ASMData asmData) {
        String className = asmData.getClassName();
        int pkgIndex = className.lastIndexOf('.');
        String packageName = className;
        if (pkgIndex > -1)
            packageName = packageName.substring(0, pkgIndex);
        packageName = packageName.replace(".", "/");
        if (!configSet.containsKey(packageName)) {
            CoreProperties.logger.info("Discovered config controller inside: {}", packageName);
            File defaultConfig = getConfigForPackage(asmData);
            CoreProperties.logger.info("Setting config: {}", defaultConfig);
            configSet.put(packageName, new ConfigContainer(packageName, defaultConfig));
        } else {
            CoreProperties.logger.warn("THIS IS AN ERROR! Ignoring {}", className);
            CoreProperties.logger.warn("Config controller discovered in the same root: {}. ", packageName);
        }
    }

    public void processIConfigRegistrar() {
        ((IConfigRegistrar) modContainer.getMod()).registerCustomAnnotations();
    }

    public void processAllRoots() {
        for (String rootPackage : configSet.keySet())
            configSet.get(rootPackage).setChildClasses(getAllChildClasses(rootPackage));
    }

    public void processConfigContainers(ASMDataTable asmDataTable, LinkedHashMap<Class<? extends Annotation>, Class<? extends ConfigEntry<? extends Annotation, ?>>> annotationMap) {
        for (ConfigContainer configContainer : configSet.values())
            configContainer.processAllClasses(asmDataTable, annotationMap);
    }

    private File getConfigForPackage(ASMData asmData) {
        Map<String, Object> annotationInfo = asmData.getAnnotationInfo();
        String configFile = (String) annotationInfo.get("configFile");
        if (Strings.isNullOrEmpty(configFile))
            return new File(CONFIG_DIR, modContainer.getModId() + ".cfg");
        if (!configFile.endsWith(".cfg"))
            configFile += ".cfg";
        return new File(CONFIG_DIR, configFile);
    }

    private Collection<String> getAllChildClasses(String rootPackage) {
        ArrayList<String> children = new ArrayList<>();
        Iterator<String> iterator = ownedClasses.iterator();
        while (iterator.hasNext()) {
            String s = iterator.next();
            if (s.startsWith(rootPackage)) {
                iterator.remove();
                children.add(s.replace("/", "."));
            }
        }
        return children;
    }

    public boolean requiresSelection() {
        return configSet.size() > 1;
    }

    public void populateList(List<ConfigContainer> containerList) {
        for (ConfigContainer configContainer : configSet.values())
            containerList.add(configContainer);
    }

    public ConfigContainer getFirstContainer() {
        Iterator<ConfigContainer> iterator = configSet.values().iterator();
        return iterator.hasNext() ? iterator.next() : null;
    }

    /**
     * Go from largest to smallest, that way when the childClasses get processed it can pull them out of the pool, without them already belonging to a more generic package.
     */
    public static final Comparator<String> dataComparator = new Comparator<String>() {
        @Override
        public int compare(String data1, String data2) {
            return data2.length() - data1.length();
        }
    };
}
