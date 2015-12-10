package me.jezza.oc.client;

import me.jezza.oc.OmnisCore;
import me.jezza.oc.common.interfaces.KeyboardAdapter;
import me.jezza.oc.common.interfaces.Request;
import me.jezza.oc.common.utils.ASM;

import java.util.concurrent.LinkedBlockingDeque;

/**
 * @author Jezza
 */
public final class Keyboard {
	private static final LinkedBlockingDeque<KeyboardRequest> REQUESTS = new LinkedBlockingDeque<>();
	protected static final Keyboard INSTANCE = new Keyboard();

	private KeyboardRequest active;

	private Keyboard() {
	}

	private void onActive() {
		OmnisCore.logger.info(active.modId + " activated Keyboard control.");
		Client.blurKeyboard();
	}

	private void release() {
		OmnisCore.logger.info(active.modId + " released Keyboard control.");
		active = null;
	}

	protected void tick() {
		if (active != null) {
			if (!active.cancelled()) {
				if (active.retrieved()) {
					KeyboardAdapter adapter = active.adapter;
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
		OmnisCore.logger.info(active.modId + " acquired Keyboard control.");
		active.acquired(true);
	}

	public static Request request(KeyboardAdapter adapter) {
		KeyboardRequest request = new KeyboardRequest(adapter);
		OmnisCore.logger.info(request.modId + " requested Keyboard control.");
		REQUESTS.add(request);
		return request;
	}

	public static class KeyboardRequest extends AbstractRequest {
		private final KeyboardAdapter adapter;
		private final String modId;

		public KeyboardRequest(KeyboardAdapter adapter) {
			this.adapter = adapter;
			modId = ASM.findOwner(adapter).getModId();
		}

		@Override
		protected void onAcquisition() {
			INSTANCE.onActive();
		}

		@Override
		public void onRelease() {
			INSTANCE.release();
		}
	}
}
