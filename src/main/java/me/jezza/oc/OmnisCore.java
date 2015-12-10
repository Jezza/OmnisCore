package me.jezza.oc;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import me.jezza.oc.client.ClientTickHandler;
import me.jezza.oc.common.CommonProxy;
import me.jezza.oc.common.core.channel.ChannelDispatcher;
import me.jezza.oc.common.core.config.Config.Controller;
import me.jezza.oc.common.core.config.ConfigHandler;
import me.jezza.oc.common.core.network.search.SearchThread;
import me.jezza.oc.common.interfaces.IChannel;
import me.jezza.oc.common.interfaces.SidedChannel;
import me.jezza.oc.common.items.ItemControl;
import me.jezza.oc.common.utils.Debug;
import org.apache.logging.log4j.Logger;

import static me.jezza.oc.common.core.CoreProperties.*;

@Controller
@Mod(modid = MOD_ID, name = MOD_NAME, version = VERSION, dependencies = DEPENDENCIES)
public class OmnisCore {

	@Instance(MOD_ID)
	public static OmnisCore instance;

	@SidedProxy(clientSide = CLIENT_PROXY, serverSide = SERVER_PROXY)
	public static CommonProxy proxy;

	@SidedChannel(MOD_ID)
	public static IChannel channel;

	public static Logger logger; //  = LogManager.getLogger(MOD_ID);

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();
		logger.info("-- Pre-Initialising " + MOD_ID + " (" + VERSION + ") --");

		logger.info("-- Initialising ConfigAnnotations --");
		ConfigHandler.init();
		logger.info("-- Checking Debug Overrides --");
		Debug.checkSysOverrides();
		logger.info("-- Configuring Channels --");
		ChannelDispatcher.init();
		new ItemControl();
	}

	@EventHandler
	public void initialize(FMLInitializationEvent event) {
		logger.info("-- Initialising --");
		logger.info("-- Starting OmnisCore|NST --");
		SearchThread.getInstance().start();

		logger.info("-- Initialising ClientTickHandler --");
		ClientTickHandler.init();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		logger.info("-- Post-Initialising --");
	}
}
