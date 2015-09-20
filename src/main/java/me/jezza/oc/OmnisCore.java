package me.jezza.oc;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import me.jezza.oc.api.configuration.Config;
import me.jezza.oc.api.configuration.ConfigHandler;
import me.jezza.oc.api.network.search.SearchThread;
import me.jezza.oc.common.CommonProxy;
import me.jezza.oc.common.core.DebugHelper;
import me.jezza.oc.common.core.network.MessageGuiNotify;
import me.jezza.oc.common.core.network.NetworkDispatcher;

import static me.jezza.oc.common.core.CoreProperties.*;

@Config.Controller()
@Mod(modid = MOD_ID, name = MOD_NAME, version = VERSION, dependencies = DEPENDENCIES)
public class OmnisCore {

    @Instance(MOD_ID)
    public static OmnisCore instance;

    @SidedProxy(clientSide = CLIENT_PROXY, serverSide = SERVER_PROXY)
    public static CommonProxy proxy;

    public static NetworkDispatcher networkDispatcher;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        logger.info("-- Pre-Initialising " + MOD_ID + " (" + VERSION + ") --");

        ConfigHandler.init(event);
        DebugHelper.checkSysOverrides();

        logger.info("Setting up internal network - Channel ID: " + MOD_ID);
        networkDispatcher = new NetworkDispatcher(MOD_ID);
        networkDispatcher.registerMessage(MessageGuiNotify.class, Side.SERVER);
        logger.info("Success! Network fully integrated.");
    }

    @EventHandler
    public void initialize(FMLInitializationEvent event) {
        logger.info("-- Initialising --");

        logger.info("Starting OmnisCore|NST");
        SearchThread.getInstance().start();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        logger.info("-- Post-Initialising --");
    }
}
