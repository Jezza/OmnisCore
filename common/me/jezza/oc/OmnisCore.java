package me.jezza.oc;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import me.jezza.oc.api.collect.Graph;
import me.jezza.oc.api.configuration.ConfigHandler;
import me.jezza.oc.api.configuration.ConfigTest;
import me.jezza.oc.api.network.NetworkCore;
import me.jezza.oc.api.network.NetworkInstance;
import me.jezza.oc.api.network.NetworkResponse;
import me.jezza.oc.common.CommonProxy;
import me.jezza.oc.common.core.CoreProperties;
import me.jezza.oc.common.core.config.CoreConfig;
import me.jezza.oc.common.core.network.MessageGuiNotify;
import me.jezza.oc.common.core.network.NetworkDispatcher;

import static me.jezza.oc.common.core.CoreProperties.*;

@Mod(modid = MOD_ID, name = MOD_NAME, version = VERSION, dependencies = DEPENDENCIES)
public class OmnisCore {

    @Mod.Instance(MOD_ID)
    public static OmnisCore instance;

    @SidedProxy(clientSide = CoreProperties.CLIENT_PROXY, serverSide = CoreProperties.SERVER_PROXY)
    public static CommonProxy proxy;

    public static NetworkDispatcher networkDispatcher;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        for (ModContainer modContainer : Loader.instance().getActiveModList()) {
        }
        logger.info("-- Pre-Initialising " + MOD_ID + " (" + VERSION + ") --");
        CoreConfig.loadConfiguration(event.getSuggestedConfigurationFile());

        logger.info("Setting up internal network - Channel ID: " + MOD_ID);
        networkDispatcher = new NetworkDispatcher(MOD_ID);
        networkDispatcher.registerMessage(MessageGuiNotify.class, Side.SERVER);
        logger.info("Success! Network fully integrated.");

        ConfigHandler configHandler = new ConfigHandler();
        configHandler.register(ConfigTest.class);
        configHandler.readFrom(event.getSuggestedConfigurationFile());

        logger.fatal(ConfigTest.testInt);
        logger.fatal(ConfigTest.testBoolean);
    }

    @Mod.EventHandler
    public void initialize(FMLInitializationEvent event) {
        logger.info("-- Initialising --");

        logger.info("Preloading Network|API");
        new NetworkInstance();
        new NetworkCore();
        new Graph<>();
        NetworkResponse.MessageResponse invalid = NetworkResponse.MessageResponse.INVALID;
        logger.info("Finished Preloading Network|API");
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        logger.info("-- Post-Initialising --");
    }

}
