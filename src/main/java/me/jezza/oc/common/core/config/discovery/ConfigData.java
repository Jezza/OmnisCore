package me.jezza.oc.common.core.config.discovery;

import com.google.common.base.Strings;
import cpw.mods.fml.common.discovery.ASMDataTable.ASMData;
import me.jezza.oc.OmnisCore;
import me.jezza.oc.common.core.config.ConfigEntry;
import me.jezza.oc.common.core.config.ICEFactory;

import java.io.File;
import java.lang.annotation.Annotation;
import java.util.*;

/**
 * The config data for any given mod.
 * Handles the loading and saving from each container within the mod.
 */
public class ConfigData {
	private static final File CONFIG_DIR = new File(".", "config");

	private final String modId;
	private Collection<String> ownedClasses;
	TreeMap<String, ConfigContainer> containerMap;

	public ConfigData(String modId, Collection<String> ownedClasses) {
		this.modId = modId;
		copyOwnedClasses(ownedClasses);
		containerMap = new TreeMap<>(LONGEST_STRING);
	}

	private void copyOwnedClasses(Collection<String> ownedClasses) {
		ArrayList<String> sortedClasses = new ArrayList<>(ownedClasses.size());
		for (String s : ownedClasses)
			if (!sortedClasses.contains(s))
				sortedClasses.add(s);
		this.ownedClasses = sortedClasses;
	}

	public void addRoot(ASMData asmData) {
		Map<String, Object> annotationInfo = asmData.getAnnotationInfo();
		String packageName = asmData.getClassName();

		int pkgIndex = packageName.lastIndexOf('.');
		if (pkgIndex > -1)
			packageName = packageName.substring(0, pkgIndex);

		if (!containerMap.containsKey(packageName)) {
			OmnisCore.logger.info("Discovered config controller inside: {}", packageName);
			File defaultConfig = getConfigForPackage(annotationInfo);
			OmnisCore.logger.info("Setting config: {}", defaultConfig);
			containerMap.put(packageName, new ConfigContainer(defaultConfig));
		} else {
			OmnisCore.logger.warn("Config controller discovered in the same root: {}. ", packageName);
			OmnisCore.logger.warn("Ignoring {}", asmData.getClassName());
		}
	}

	public ConfigData processRoots() {
		for (String rootPackage : containerMap.keySet())
			containerMap.get(rootPackage).setChildClasses(getAllChildClasses(rootPackage));
		return this;
	}

	public void processConfigContainers(Collection<ICEFactory<?, ? extends ConfigEntry<? extends Annotation, ?>>> annotationMap) {
		for (ConfigContainer configContainer : containerMap.values()) {
			configContainer.processAllClasses(annotationMap);
			configContainer.operateOnConfig(false);
		}
	}

	public void load() {
		for (ConfigContainer configContainer : containerMap.values())
			configContainer.operateOnConfig(false);
	}

	public void save() {
		for (ConfigContainer configContainer : containerMap.values())
			configContainer.operateOnConfig(true);
	}

	private File getConfigForPackage(Map<String, Object> annotationInfo) {
		String configFile = (String) annotationInfo.get("configFile");
		if (Strings.isNullOrEmpty(configFile))
			return new File(CONFIG_DIR, modId + ".cfg");
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

	/**
	 * Go from largest to smallest, that way when the childClasses get processed it can pull them out of the pool, without them already belonging to a more generic package.
	 */
	public static final Comparator<String> LONGEST_STRING = new Comparator<String>() {
		@Override
		public int compare(String data1, String data2) {
			return data2.length() - data1.length();
		}
	};
}
