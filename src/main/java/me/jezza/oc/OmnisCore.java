package me.jezza.oc;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import me.jezza.oc.api.channel.IChannel;
import me.jezza.oc.api.channel.SidedChannel;
import me.jezza.oc.api.config.Config.Controller;
import me.jezza.oc.api.network.search.SearchThread;
import me.jezza.oc.common.CommonProxy;
import me.jezza.oc.common.utils.helpers.DebugHelper;
import me.jezza.oc.common.core.channel.ChannelDispatcher;
import me.jezza.oc.common.core.config.ConfigHandler;

import static me.jezza.oc.common.core.CoreProperties.*;

@Controller()
@Mod(modid = MOD_ID, name = MOD_NAME, version = VERSION, dependencies = DEPENDENCIES)
public class OmnisCore {

    @Instance(MOD_ID)
    public static OmnisCore instance;

    @SidedProxy(clientSide = CLIENT_PROXY, serverSide = SERVER_PROXY)
    public static CommonProxy proxy;

    @SidedChannel(MOD_ID)
    public static IChannel channel;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        logger.info("-- Pre-Initialising " + MOD_ID + " (" + VERSION + ") --");

        logger.info("-- Initialising ConfigAnnotations --");
        ConfigHandler.init(event);
        logger.info("-- Checking Debug Overrides --");
        DebugHelper.checkSysOverrides();
        logger.info("-- Configuring Internal Channels --");
        ChannelDispatcher.init(event);
    }

    @EventHandler
    public void initialize(FMLInitializationEvent event) {
        logger.info("-- Initialising --");
        logger.info("-- Starting OmnisCore|NST --");
        SearchThread.getInstance().start();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        logger.info("-- Post-Initialising --");

        logger.info("-- Locking Channel packet registration. --");
        ChannelDispatcher.lockdown(event);
    }
}
