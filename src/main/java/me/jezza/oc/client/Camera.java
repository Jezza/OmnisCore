package me.jezza.oc.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import me.jezza.oc.OmnisCore;
import me.jezza.oc.client.lib.AbstractResourceRequest;

import java.util.concurrent.LinkedBlockingDeque;

/**
 * @author Jezza
 */
@SideOnly(Side.CLIENT)
public class Camera {
	private static final LinkedBlockingDeque<CameraRequest> REQUESTS = new LinkedBlockingDeque<>();
	private static final Camera INSTANCE = new Camera();

	private CameraRequest active;

	private Camera() {
	}

	private void onActive() {
		OmnisCore.logger.info(active.modId + " activated Keyboard control.");
		Client.blurKeyboard();
	}

	private void release() {
		OmnisCore.logger.info(active.modId + " released Keyboard control.");
		active = null;
	}

	public static CameraRequest request() {
		CameraRequest request = new CameraRequest();
		OmnisCore.logger.info(request.modId + " requested Camera control.");
		REQUESTS.add(request);
		return request;
	}

	public static class CameraRequest extends AbstractResourceRequest<Camera> {
		private final String modId = "Something";

		public CameraRequest() {
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
