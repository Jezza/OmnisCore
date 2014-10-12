package me.jezza.dc;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import me.jezza.dc.common.CommonProxy;
import me.jezza.dc.common.core.CoreProperties;
import me.jezza.dc.common.core.config.CoreConfig;
import me.jezza.dc.common.core.network.NetworkDispatcher;
import me.jezza.dc.common.core.network.NetworkHelper;
import me.jezza.dc.common.items.debug.ItemDebug;

import static me.jezza.dc.common.core.CoreProperties.*;

@Mod(modid = MOD_ID, name = MOD_NAME, version = VERSION, dependencies = DEPENDENCIES)
public class DeusCore {

    @Mod.Instance(MOD_ID)
    public static DeusCore instance;

    @SidedProxy(clientSide = CoreProperties.CLIENT_PROXY, serverSide = CoreProperties.SERVER_PROXY)
    public static CommonProxy proxy;

    public static NetworkDispatcher networkDispatcher;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger.info("-- Preparing DeusCore (" + VERSION + ") --");
        logger.info("-- Pre-Initialising --");
        CoreConfig.loadConfiguration(event.getSuggestedConfigurationFile());


        logger.info("Setting up internal network - Channel ID: " + MOD_ID);
        networkDispatcher = new NetworkDispatcher(MOD_ID);
        NetworkHelper.init();
        logger.info("Success! Network fully integrated.");

        new ItemDebug("debugItem");
    }

    @Mod.EventHandler
    public void initialize(FMLInitializationEvent event) {
        logger.info("-- Initialising --");
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        logger.info("-- Post-Initialising --");
    }

}
