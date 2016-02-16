package me.jezza.oc.common.core.config.discovery;

import static me.jezza.oc.common.utils.helpers.StringHelper.useable;

import java.io.File;
import java.lang.annotation.Annotation;
import java.util.*;
import java.util.Map.Entry;

import cpw.mods.fml.common.discovery.ASMDataTable.ASMData;
import me.jezza.oc.OmnisCore;
import me.jezza.oc.common.core.config.ConfigEntry;
import me.jezza.oc.common.core.config.ICEFactory;
import me.jezza.oc.common.interfaces.InputBuffer;
import me.jezza.oc.common.interfaces.OutputBuffer;
import net.minecraft.entity.player.EntityPlayer;
import org.apache.commons.lang3.ClassUtils;

/**
 * The config data for any given mod.
 * Handles the loading and saving from each container within the mod.
 */
public class ConfigData {
	private static final File CONFIG_DIR = new File(".", "config");

	private final String modId;
	private Collection<String> ownedClasses;
	private Map<String, ConfigContainer> containerMap;

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
		sortedClasses.trimToSize();
		this.ownedClasses = sortedClasses;
	}

	public void addRoot(ASMData data) {
		String packageName = ClassUtils.getPackageName(data.getClassName());
		if (!containerMap.containsKey(packageName)) {
			OmnisCore.logger.info("Discovered config controller inside: {}", packageName);
			File config = configForPackage(data.getAnnotationInfo());
			OmnisCore.logger.info("Setting config: {}", config);
			containerMap.put(packageName, new ConfigContainer(config));
		} else {
			OmnisCore.logger.warn("Config controller discovered in the same root: {}. ", packageName);
			OmnisCore.logger.warn("Ignoring {}", data.getClassName());
		}
	}

	public ConfigData processRoots() {
		for (Entry<String, ConfigContainer> entry : containerMap.entrySet())
			entry.getValue().childClasses(classes(entry.getKey()));
		return this;
	}

	public <A extends Annotation, C extends ConfigEntry<A, ?>> void processConfigContainers(Collection<ICEFactory<A, C>> annotationMap) {
		for (ConfigContainer configContainer : containerMap.values())
			configContainer.processAllClasses(annotationMap).operate(false);
	}

	public void operate(boolean saveFlag) {
		for (ConfigContainer configContainer : containerMap.values())
			configContainer.operate(saveFlag);
	}

	public void writeSync(EntityPlayer player, OutputBuffer buffer) {
		for (ConfigContainer configContainer : containerMap.values())
			configContainer.writeSync(player, buffer);
	}

	public void readSync(InputBuffer buffer) {
		for (ConfigContainer configContainer : containerMap.values())
			configContainer.readSync(buffer);
	}

	private File configForPackage(Map<String, Object> annotationInfo) {
		String configFile = (String) annotationInfo.get("configFile");
		if (!useable(configFile))
			return new File(CONFIG_DIR, modId + ".cfg");
		if (!configFile.endsWith(".cfg"))
			configFile += ".cfg";
		return new File(CONFIG_DIR, configFile);
	}

	private Collection<String> classes(String rootPackage) {
		List<String> children = new ArrayList<>(8);
		Iterator<String> it = ownedClasses.iterator();
		while (it.hasNext()) {
			String s = it.next();
			if (s.startsWith(rootPackage)) {
				it.remove();
				children.add(s.replace('/', '.'));
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
