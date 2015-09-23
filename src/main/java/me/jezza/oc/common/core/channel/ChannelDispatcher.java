package me.jezza.oc.common.core.channel;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.discovery.ASMDataTable;
import cpw.mods.fml.common.discovery.ASMDataTable.ASMData;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import me.jezza.oc.api.channel.IChannel;
import me.jezza.oc.api.channel.SidedChannel;
import me.jezza.oc.common.core.CoreProperties;
import me.jezza.oc.common.core.channel.internal.ChannelFML;
import me.jezza.oc.common.core.channel.internal.ChannelMC;
import me.jezza.oc.common.utils.helpers.StringHelper;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Jezza
 */
public class ChannelDispatcher {
    private static ChannelDispatcher INSTANCE;

    private static final Map<String, IChannel> channelMap = new HashMap<>();
    private static Map<String, ModContainer> indexedModMap;

    private static boolean lockdown = false;

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
                    CoreProperties.logger.warn("Non static field");
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
        if (!(StringHelper.useable(modId) && Loader.isModLoaded(modId))) {
            CoreProperties.logger.warn("Something attempted to access a Channel for a mod that doesn't exist: " + String.valueOf(modId));
            return null;
        }
        if (modId.startsWith("MC|"))
            return minecraft();
        if (modId.startsWith("FML"))
            return fml();
        if (modId.startsWith("\u0001"))
            throw new IllegalArgumentException("Not a valid channel name: " + modId);
        IChannel channel = channelMap.get(modId);
        if (channel == null) {
            ModContainer mod = getIndexedModMap().get(modId);
            channel = new OmnisChannel(mod, modId, FMLCommonHandler.instance().getSide(), new OmnisCodec());
            channelMap.put(modId, channel);
        }
        return channel;
    }

    /**
     * This way only one unmodifiable list is created.
     * @return A ImmutableMap of all mods, mapped to their modId.
     */
    public static Map<String, ModContainer> getIndexedModMap() {
        return indexedModMap != null ? indexedModMap : (indexedModMap = Loader.instance().getIndexedModList());
    }


    public static ChannelMC minecraft() {
        throw new UnsupportedOperationException("Not Yet Implemented!");
    }

    public static ChannelFML fml() {
        throw new UnsupportedOperationException("Not Yet Implemented!");
    }

    public static void lockdown(FMLPostInitializationEvent event) {
        lockdown = lockdown || event != null;
    }

    public static boolean lockdown() {
        return lockdown;
    }
}
