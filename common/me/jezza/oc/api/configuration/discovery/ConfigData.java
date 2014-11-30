package me.jezza.oc.api.configuration.discovery;

import com.google.common.collect.Lists;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.discovery.ASMDataTable.ASMData;
import me.jezza.oc.api.configuration.Config.IConfigRegistrar;
import me.jezza.oc.api.configuration.ConfigEntry;
import me.jezza.oc.common.core.CoreProperties;

import java.io.File;
import java.lang.annotation.Annotation;
import java.util.*;

public class ConfigData {
    private static final File CONFIG_DIR = new File(".", "config");

    private Collection<String> ownedClasses;
    TreeMap<String, ConfigContainer> treeSet;
    private ModContainer modContainer;
    public final boolean isRegistrar;

    public ConfigData(ModContainer modContainer, Set<String> ownedClasses) {
        this.modContainer = modContainer;
        this.ownedClasses = Lists.newArrayList(ownedClasses);
        isRegistrar = modContainer.getMod() instanceof IConfigRegistrar;
        treeSet = new TreeMap<>(dataComparator);
    }

    public void addRoot(ASMData asmData) {
        String className = asmData.getClassName();
        int pkgIndex = className.lastIndexOf('.');
        String packageName = className;
        if (pkgIndex > -1)
            packageName = packageName.substring(0, pkgIndex);
        packageName = packageName.replace(".", "/");
        if (!treeSet.containsKey(packageName)) {
            CoreProperties.logger.info("Discovered config controller inside: {}", packageName);
            File defaultConfig = getConfigDirForPackage(asmData);
            CoreProperties.logger.info("Setting config: {}", defaultConfig);
            treeSet.put(packageName, new ConfigContainer(defaultConfig));
        } else {
            CoreProperties.logger.warn("Config controller discovered in the same root: {}. ", packageName);
            CoreProperties.logger.warn("THIS IS AN ERROR! Ignoring {}", className);
        }
    }

    public void processIConfigRegistrar() {
        ((IConfigRegistrar) modContainer.getMod()).registerCustomAnnotations();
    }

    public void processAllRoots() {
        for (String rootPackage : treeSet.keySet())
            treeSet.get(rootPackage).setChildClasses(getAllChildClasses(rootPackage));
    }

    public void processConfigContainers(LinkedHashMap<Class<? extends Annotation>, Class<? extends ConfigEntry<? extends Annotation, ?>>> annotationMap) {
        for (ConfigContainer configContainer : treeSet.values())
            configContainer.processAllClasses(annotationMap);
    }

    private File getConfigDirForPackage(ASMData asmData) {
        Map<String, Object> annotationInfo = asmData.getAnnotationInfo();
        String configFile = (String) annotationInfo.get("configFile");
        if (configFile == null || configFile.equals(""))
            return new File(CONFIG_DIR, modContainer.getModId() + ".cfg");
        return new File(CONFIG_DIR, configFile);
    }

    private Collection<String> getAllChildClasses(String rootPackage) {
        ArrayList<String> classes = new ArrayList<>();
        Iterator<String> iterator = ownedClasses.iterator();
        while (iterator.hasNext()) {
            String s = iterator.next();
            if (s.startsWith(rootPackage)) {
                iterator.remove();
                classes.add(s.replace("/", "."));
            }
        }
        return classes;
    }

    private static Comparator<String> dataComparator = new Comparator<String>() {
        @Override
        public int compare(String data1, String data2) {
            return data2.length() - data1.length();
        }
    };

    @Override
    public String toString() {
        return modContainer.getModId() + ": " + treeSet.toString();
    }
}
