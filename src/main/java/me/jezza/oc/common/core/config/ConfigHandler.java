package me.jezza.oc.common.core.config;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.discovery.ASMDataTable.ASMData;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ClientDisconnectionFromServerEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import me.jezza.oc.OmnisCore;
import me.jezza.oc.common.core.config.discovery.ConfigData;
import me.jezza.oc.common.core.intern.ConfigSyncPacket;
import me.jezza.oc.common.interfaces.InputBuffer;
import me.jezza.oc.common.interfaces.OutputBuffer;
import me.jezza.oc.common.utils.ASM;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.config.Configuration;
import org.apache.commons.lang3.ClassUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.Map.Entry;

import static me.jezza.oc.common.core.config.Config.ConfigAnnotation;
import static me.jezza.oc.common.core.config.Config.Controller;
import static me.jezza.oc.common.utils.helpers.StringHelper.format;
import static me.jezza.oc.common.utils.helpers.StringHelper.useable;

public final class ConfigHandler {
	private static ConfigHandler INSTANCE;

	// After all annotated fields have been located, processed, and cached.
	private static boolean postProcessed = false;

	private static final Map<String, ConfigData> configMap = new LinkedHashMap<>();
	private static final List<ICEFactory<?, ? extends ConfigEntry<? extends Annotation, ?>>> annotations = new LinkedList<>();

	public static void init() {
		if (INSTANCE != null)
			return;
		INSTANCE = new ConfigHandler();
		INSTANCE.parseControllers();
		FMLCommonHandler.instance().bus().register(INSTANCE);
	}

	private ConfigHandler() {
	}

	@SuppressWarnings("unchecked")
	private void parseControllers() {
		// Maps all the controller annotations to their associated mod.
		for (ASMData data : ASM.dataTable(Controller.class)) {
			final String packageName = ClassUtils.getPackageName(data.getClassName());
			String modId = ASM.findOwner(packageName).getModId();
			ConfigData configData = configMap.get(modId);
			if (configData == null) {
				configData = new ConfigData(modId, ASM.ownedClasses(packageName));
				configMap.put(modId, configData);
			}
			configData.addRoot(data);
		}

		// Process all config annotations.
		for (Entry<ASMData, Class<?>> entry : ASM.classesWith(ConfigAnnotation.class).entrySet()) {
			Class<? extends Annotation> annotationClass = (Class<? extends Annotation>) entry.getValue();
			ConfigAnnotation annotation = annotationClass.getAnnotation(ConfigAnnotation.class);
			if (annotation == null)
				throw new IllegalStateException(format("Configuration was supplied an invalid configuration annotation. {}"));
			Class<? extends ConfigEntry<? extends Annotation, ?>> configEntryClass = annotation.value();
			if (Modifier.isAbstract(configEntryClass.getModifiers()))
				throw new IllegalStateException(format("Supplied ConfigEntry ({}) is abstract.", configEntryClass.getCanonicalName()));
			annotations.add(createFactory(annotationClass, (Class) configEntryClass));
		}

		// Organise all sub-packages, and process all classes associated with the ConfigContainer.
		for (ConfigData configData : configMap.values())
			configData.processRoots().processConfigContainers((Collection) annotations);
		postProcessed = true;
	}

	private static <A extends Annotation, T extends ConfigEntry<A, ?>> ICEFactory<A, T> createFactory(final Class<A> annotationClass, final Class<T> configEntryClass) {
		try {
			final Constructor<T> constructor = configEntryClass.getDeclaredConstructor(OmnisConfiguration.class);
			constructor.setAccessible(true);
			return new ICEFactory<A, T>() {
				@Override
				public T create(Object... params) throws InstantiationException, IllegalAccessException, InvocationTargetException {
					return constructor.newInstance(params);
				}

				@Override
				public Class<A> annotationClass() {
					return annotationClass;
				}
			};
		} catch (NoSuchMethodException e) {
			throw new ConfigurationException(format("Invalid constructor; Expected constructor with {} in {}.", Configuration.class.getCanonicalName(), configEntryClass));
		}
	}

	public static boolean load(String modID) {
		if (postProcessed && useable(modID) && Loader.isModLoaded(modID)) {
			ConfigData configData = configMap.get(modID);
			if (configData != null) {
				configData.operate(false);
				return true;
			}
		}
		return false;
	}

	public static boolean save(String modID) {
		if (postProcessed && useable(modID) && Loader.isModLoaded(modID)) {
			ConfigData configData = configMap.get(modID);
			if (configData != null) {
				configData.operate(true);
				return true;
			}
		}
		return false;
	}

	@SubscribeEvent
	@SideOnly(Side.SERVER)
	public void onPlayerLogin(PlayerLoggedInEvent event) {
		EntityPlayer player = event.player;
		OmnisCore.channel.sendTo(new ConfigSyncPacket(player), player);
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onClientDisconnect(ClientDisconnectionFromServerEvent event) {
		for (ConfigData configData : configMap.values())
			configData.operate(false);
	}

	public static void writeSync(EntityPlayer player, OutputBuffer output) {
		for (ConfigData configData : configMap.values())
			configData.writeSync(player, output);
	}

	public static void readSync(InputBuffer output) {
		for (ConfigData configData : configMap.values())
			configData.readSync(output);
	}

	@Override
	public String toString() {
		return annotations.toString();
	}
}
