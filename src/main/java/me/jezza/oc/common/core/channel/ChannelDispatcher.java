package me.jezza.oc.common.core.channel;

import com.google.common.base.Throwables;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.discovery.ASMDataTable.ASMData;
import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import me.jezza.oc.OmnisCore;
import me.jezza.oc.common.core.config.Config.ConfigDouble;
import me.jezza.oc.common.interfaces.IChannel;
import me.jezza.oc.common.interfaces.SidedChannel;
import me.jezza.oc.common.utils.ASM;
import me.jezza.oc.common.utils.Mods;
import me.jezza.oc.common.utils.helpers.StringHelper;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import static cpw.mods.fml.common.LoaderState.POSTINITIALIZATION;
import static me.jezza.oc.common.utils.helpers.StringHelper.format;

/**
 * @author Jezza
 */
public final class ChannelDispatcher {
	public static final String OC_CHANNEL_SUFFIX = "|OC";

	private static ChannelDispatcher INSTANCE;

	@ConfigDouble(category = "Networking", minValue = 5, maxValue = 120, comment = "The default network update range.")
	protected static double NETWORK_UPDATE_RANGE = 60;

	private static final Map<Side, Map<String, IChannel>> channelMap = new EnumMap<>(Side.class);

	static {
		channelMap.put(Side.CLIENT, new HashMap<String, IChannel>());
		channelMap.put(Side.SERVER, new HashMap<String, IChannel>());
	}

	public static void init() {
		if (INSTANCE != null)
			return;
		INSTANCE = new ChannelDispatcher();
		INSTANCE.parseControllers();
	}

	private ChannelDispatcher() {
	}

	private void parseControllers() {
		for (Entry<ASMData, Field> entry : ASM.fieldsWith(SidedChannel.class).entrySet()) {
			try {
				Field field = entry.getValue();
				int mods = field.getModifiers();
				if (!Modifier.isStatic(mods)) {
					OmnisCore.logger.warn(format("Discovered @{} on a non-static field. Skipping...", SidedChannel.class.getSimpleName()));
					continue;
				}
				if (Modifier.isFinal(mods)) {
					OmnisCore.logger.warn(format("Discovered @{} on a final field. Skipping...", SidedChannel.class.getSimpleName()));
					continue;
				}
				String value = entry.getKey().getAnnotationInfo().get("value").toString();
				OmnisCore.logger.warn(format("Injecting channel in {} with @{}({}).", field.getDeclaringClass().getCanonicalName() + '.' + field.getName(), SidedChannel.class.getSimpleName(), value));
				field.set(null, channel(value));
			} catch (IllegalAccessException e) {
				throw Throwables.propagate(e);
			}
		}
	}

	public static IChannel channel(String modId) {
		return channel(modId, FMLCommonHandler.instance().getSide());
	}

	public static IChannel channel(String modId, Side source) {
		if (!(StringHelper.useable(modId) && Loader.isModLoaded(modId))) {
			OmnisCore.logger.warn("Something attempted to access a Channel for a mod that doesn't exist: " + String.valueOf(modId));
			return null;
		}
		if (modId.startsWith("MC|"))
			return minecraft();
		if (modId.startsWith("FML"))
			return fml();
		if (modId.startsWith("\u0001"))
			throw new IllegalArgumentException("Not a valid channel name: " + modId);
		IChannel channel = channelMap.get(source).get(modId);
		if (ASM.hasReachedState(POSTINITIALIZATION) || channel != null)
			return channel;
		ModContainer mod = Mods.get(modId);
		EnumMap<Side, FMLEmbeddedChannel> sidedChannelMap = NetworkRegistry.INSTANCE.newChannel(mod, modId + OC_CHANNEL_SUFFIX, new OmnisCodec(modId));
		for (Entry<Side, FMLEmbeddedChannel> entry : sidedChannelMap.entrySet())
			channelMap.get(entry.getKey()).put(modId, new OmnisChannel(entry.getValue()));
		return channelMap.get(source).get(modId);
	}

	public static IChannel minecraft() {
		throw new UnsupportedOperationException("Not Yet Implemented!");
	}

	public static IChannel fml() {
		throw new UnsupportedOperationException("Not Yet Implemented!");
	}

	public static double range() {
		return NETWORK_UPDATE_RANGE;
	}
}
