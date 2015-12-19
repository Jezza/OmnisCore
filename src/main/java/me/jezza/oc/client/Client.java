package me.jezza.oc.client;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.RenderTickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

import static org.lwjgl.input.Keyboard.*;

@SideOnly(Side.CLIENT)
public final class Client {
	private static final Minecraft MC = Minecraft.getMinecraft();
	private static final Client INSTANCE = new Client();
	private static boolean init = false;

	public static void init() {
		if (init)
			return;
		init = true;
		FMLCommonHandler.instance().bus().register(INSTANCE);
	}

	private Client() {
	}

	@SubscribeEvent
	public void tick(ClientTickEvent event) {
		me.jezza.oc.client.Keyboard.INSTANCE.tick(event);
		Mouse.INSTANCE.tick(event);
	}

	@SubscribeEvent
	public void renderTick(RenderTickEvent event) {
		Camera.INSTANCE.tick(event);
	}

	public static boolean hasPressedShift() {
		return Minecraft.isRunningOnMac ? Keyboard.isKeyDown(KEY_LWIN) || Keyboard.isKeyDown(KEY_RMETA) : Keyboard.isKeyDown(KEY_LCONTROL) || Keyboard.isKeyDown(KEY_RCONTROL);
	}

	public static boolean hasPressedCtrl() {
		return Keyboard.isKeyDown(KEY_LCONTROL) || Keyboard.isKeyDown(KEY_RCONTROL);
	}

	public static boolean hasPressedAlt() {
		return Keyboard.isKeyDown(KEY_LMENU) || Keyboard.isKeyDown(KEY_RMENU);
	}

	public static boolean isPlayer(String name) {
		return Minecraft.getMinecraft().thePlayer.getCommandSenderName().equals(name);
	}

	public static void blurGame() {
		MC.inGameHasFocus = false;
	}

	public static void focusGame() {
		MC.inGameHasFocus = true;
	}

	public static void blurKeyboard() {
		KeyBinding.unPressAllKeys();
	}

	public static void blurMouse() {
		MC.mouseHelper.ungrabMouseCursor();
	}

	public static void focusMouse() {
		MC.mouseHelper.grabMouseCursor();
	}

	public static void clear() {
		MC.displayGuiScreen(null);
	}
}
