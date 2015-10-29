package me.jezza.oc;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import me.jezza.oc.common.interfaces.IChannel;
import me.jezza.oc.common.interfaces.SidedChannel;
import me.jezza.oc.common.core.config.Config.Controller;
import me.jezza.oc.common.core.network.search.SearchThread;
import me.jezza.oc.common.CommonProxy;
import me.jezza.oc.common.core.channel.ChannelDispatcher;
import me.jezza.oc.common.core.config.ConfigHandler;
import me.jezza.oc.common.items.ItemAbstract;
import me.jezza.oc.common.utils.helpers.DebugHelper;
import net.minecraft.creativetab.CreativeTabs;

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
		ConfigHandler.init();
		logger.info("-- Checking Debug Overrides --");
		DebugHelper.checkSysOverrides();
		logger.info("-- Configuring Internal Channels --");
		ChannelDispatcher.init();
		new ItemTest();
	}

	public static class ItemTest extends ItemAbstract {
		public ItemTest() {
			super("Test");
			textureless(true);
			setCreativeTab(CreativeTabs.tabMisc);
		}
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
