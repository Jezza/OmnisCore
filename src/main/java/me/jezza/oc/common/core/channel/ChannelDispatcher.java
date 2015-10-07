package me.jezza.oc.common.core.channel;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.discovery.ASMDataTable;
import cpw.mods.fml.common.discovery.ASMDataTable.ASMData;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import me.jezza.oc.api.channel.IChannel;
import me.jezza.oc.api.channel.SidedChannel;
import me.jezza.oc.api.config.Config.ConfigDouble;
import me.jezza.oc.common.core.CoreProperties;
import me.jezza.oc.common.utils.helpers.ModHelper;
import me.jezza.oc.common.utils.helpers.StringHelper;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import static me.jezza.oc.common.utils.helpers.StringHelper.format;

/**
 * @author Jezza
 */
public final class ChannelDispatcher {
    private static ChannelDispatcher INSTANCE;

    @ConfigDouble(category = "Networking", minValue = 5, maxValue = 120, comment = "The default network update range.")
    protected static double NETWORK_UPDATE_RANGE = 60;

    private static final Map<Side, Map<String, IChannel>> channelMap = new EnumMap<>(Side.class);
    private static boolean lockdown = false;

    static {
        channelMap.put(Side.CLIENT, new HashMap<String, IChannel>());
        channelMap.put(Side.SERVER, new HashMap<String, IChannel>());
    }

    public static void init(FMLPreInitializationEvent event) {
        if (INSTANCE != null)
            return;
        INSTANCE = new ChannelDispatcher();
        INSTANCE.parseControllers(event);
    }

    private ChannelDispatcher() {
    }

    private void parseControllers(FMLPreInitializationEvent event) {
        ASMDataTable dataTable = event.getAsmData();
        Set<ASMData> dataSet = dataTable.getAll(SidedChannel.class.getName());

        for (ASMData data : dataSet) {
            try {
                Class<?> clazz = Class.forName(data.getClassName());
                Field field = clazz.getDeclaredField(data.getObjectName());
                if (!Modifier.isStatic(field.getModifiers())) {
                    CoreProperties.logger.warn(format("Discovered @{} on a non-static field. Skipping...", SidedChannel.class.getSimpleName()));
                    continue;
                }
                if (Modifier.isFinal(field.getModifiers())) {
                    CoreProperties.logger.warn(format("Discovered @{} on a final field. Skipping...", SidedChannel.class.getSimpleName()));
                    continue;
                }
                field.setAccessible(true);
                field.set(null, channel(data.getAnnotationInfo().get("value").toString()));
            } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException impossible) {
                throw new IllegalStateException(impossible);
            }
        }
    }

    public static IChannel channel(String modId) {
        return channel(modId, FMLCommonHandler.instance().getSide());
    }

    public static IChannel channel(String modId, Side source) {
        if (!(StringHelper.useable(modId) && Loader.isModLoaded(modId))) {
            CoreProperties.logger.warn("Something attempted to access a Channel for a mod that doesn't exist: " + String.valueOf(modId));
            return null;
        }
        if (modId.startsWith("MC|"))
//            return minecraft();
            return null;
        if (modId.startsWith("FML"))
//            return fml();
            return null;
        if (modId.startsWith("\u0001"))
            throw new IllegalArgumentException("Not a valid channel name: " + modId);
        IChannel channel = channelMap.get(source).get(modId);
        if (lockdown || channel != null)
            return channel;
        ModContainer mod = ModHelper.getIndexedModMap().get(modId);
        OmnisCodec codec = new OmnisCodec();
        EnumMap<Side, FMLEmbeddedChannel> sidedChannelMap = NetworkRegistry.INSTANCE.newChannel(mod, modId + "|OmnisCodec", codec);
        for (Entry<Side, FMLEmbeddedChannel> entry : sidedChannelMap.entrySet())
            channelMap.get(entry.getKey()).put(modId, new OmnisChannel(entry.getValue(), codec));
        return channelMap.get(source).get(modId);
    }

    public static IChannel minecraft() {
        throw new UnsupportedOperationException("Not Yet Implemented!");
    }

    public static IChannel fml() {
        throw new UnsupportedOperationException("Not Yet Implemented!");
    }

    public static void lockdown(FMLPostInitializationEvent event) {
        if (!lockdown && event != null) {
            for (IChannel channel : channelMap.get(Side.SERVER).values())
                channel.lockdown();
            for (IChannel channel : channelMap.get(Side.CLIENT).values())
                channel.lockdown();
            lockdown = true;
        }
    }

    public static double networkUpdateRange() {
        return NETWORK_UPDATE_RANGE;
    }
}
