package me.jezza.oc.client.keyboard;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import net.minecraft.client.Minecraft;

import java.util.concurrent.LinkedBlockingDeque;

/**
 * @author Jezza
 */
public class Keyboard {
	private static final LinkedBlockingDeque<Request<Keyboard>> REQUESTS = new LinkedBlockingDeque<>();
	private static final Keyboard INSTANCE = new Keyboard();
	private static boolean init = false;

	private final Minecraft mc = Minecraft.getMinecraft();

	private Request<Keyboard> active;

	private void onActive() {
		mc.setIngameNotInFocus();
		mc.skipRenderWorld = true;

	}

	public void release() {
		active.set(null);
		active = null;
		mc.setIngameFocus();
		mc.skipRenderWorld = false;
//		KeyBinding.unPressAllKeys();
//		this.inGameHasFocus = false;
//		this.mouseHelper.ungrabMouseCursor();
	}

	@SubscribeEvent
	public void tick(ClientTickEvent event) {
		if (event.phase != Phase.START)
			return;
		if (active != null) {

		}
		if (REQUESTS.isEmpty())
			return;
		while ((active == null || active.isCancelled()) && !REQUESTS.isEmpty())
			active = REQUESTS.pollFirst();
		if (active == null)
			return;
		if (active.isCancelled()) {
			active = null;
			return;
		}
		active.set(this);
		onActive();
	}

	public static void init() {
		if (init)
			return;
		init = true;
		FMLCommonHandler.instance().bus().register(INSTANCE);
	}

	public static KeyboardRequest request() {
		KeyboardRequest request = new KeyboardRequest();
		REQUESTS.add(request);
		return request;
	}

	public static class KeyboardRequest extends Request<Keyboard> {

		@Override
		protected void onRetrieval() {
			INSTANCE.onActive();
		}
	}
}
