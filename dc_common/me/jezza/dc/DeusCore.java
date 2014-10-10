package me.jezza.dc;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import me.jezza.dc.common.CommonProxy;
import me.jezza.dc.common.core.CoreProperties;
import me.jezza.dc.common.core.config.CoreConfig;

import static me.jezza.dc.common.core.CoreProperties.*;

@Mod(modid = MOD_ID, name = MOD_NAME, version = VERSION, dependencies = DEPENDENCIES)
public class DeusCore {

    @Mod.Instance(MOD_ID)
    public static DeusCore instance;

    @SidedProxy(clientSide = CoreProperties.CLIENT_PROXY, serverSide = CoreProperties.SERVER_PROXY)
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger.info("-- Initialising DeusCore (" + VERSION + ") --");
        CoreConfig.loadConfiguration(event.getSuggestedConfigurationFile());
    }

    @Mod.EventHandler
    public void initialize(FMLInitializationEvent event) {
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
    }

}
