package me.jezza.oc.client;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;

/**
 * @author Jezza
 */
public class ClientTickHandler {
	private static final ClientTickHandler INSTANCE = new ClientTickHandler();
	private static boolean init = false;

	public static void init() {
		if (init)
			return;
		init = true;
		FMLCommonHandler.instance().bus().register(INSTANCE);
	}

	@SubscribeEvent
	public void tick(ClientTickEvent event) {
		if (event.phase != Phase.START)
			return;
		Keyboard.INSTANCE.tick();
		Mouse.INSTANCE.tick();
	}
}
