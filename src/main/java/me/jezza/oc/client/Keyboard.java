package me.jezza.oc.client;

import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import me.jezza.oc.OmnisCore;
import me.jezza.oc.client.lib.AbstractAdapterRequest;
import me.jezza.oc.common.interfaces.AdapterRequest;
import me.jezza.oc.common.interfaces.KeyboardAdapter;

import java.util.concurrent.LinkedBlockingDeque;

/**
 * @author Jezza
 */
@SideOnly(Side.CLIENT)
public final class Keyboard {
	private static final LinkedBlockingDeque<KeyboardRequest> REQUESTS = new LinkedBlockingDeque<>();
	protected static final Keyboard INSTANCE = new Keyboard();

	private KeyboardRequest active;

	private Keyboard() {
	}

	private void activate() {
		OmnisCore.logger.info(active.modId() + " activated Keyboard control.");
		Client.blurKeyboard();
	}

	private void release() {
		OmnisCore.logger.info(active.modId() + " released Keyboard control.");
		active = null;
	}

	protected void tick(ClientTickEvent event) {
		if (event.phase != Phase.START)
			return;
		if (active != null) {
			if (!active.cancelled()) {
				if (active.retrieved()) {
					KeyboardAdapter adapter = active.adapter();
					while (org.lwjgl.input.Keyboard.next()) {
						if (org.lwjgl.input.Keyboard.getEventKeyState())
							adapter.keyPress(org.lwjgl.input.Keyboard.getEventCharacter(), org.lwjgl.input.Keyboard.getEventKey());
					}
				}
			} else {
				active.release();
			}
			return;
		}
		if (REQUESTS.isEmpty())
			return;
		while ((active == null || active.cancelled()) && !REQUESTS.isEmpty())
			active = REQUESTS.pollFirst();
		if (active == null)
			return;
		if (active.cancelled()) {
			active = null;
			return;
		}
		OmnisCore.logger.info(active.modId() + " acquired Keyboard control.");
		active.acquired(true);
	}

	public static AdapterRequest request(KeyboardAdapter adapter) {
		KeyboardRequest request = new KeyboardRequest(adapter);
		OmnisCore.logger.info(request.modId() + " requested Keyboard control.");
		REQUESTS.add(request);
		return request;
	}

	public static class KeyboardRequest extends AbstractAdapterRequest<KeyboardAdapter> {
		public KeyboardRequest(KeyboardAdapter adapter) {
			super(adapter);
		}

		@Override
		protected void onAcquisition() {
			INSTANCE.activate();
		}

		@Override
		protected void onRelease() {
			INSTANCE.release();
		}

		@Override
		protected void acquired(boolean acquired) {
			super.acquired(acquired);
		}
	}
}
