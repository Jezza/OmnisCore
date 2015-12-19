package me.jezza.oc.client;

import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.RenderTickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import me.jezza.oc.OmnisCore;
import me.jezza.oc.client.lib.AbstractResourceRequest;
import me.jezza.oc.common.interfaces.CameraControl;
import me.jezza.oc.common.utils.ASM;

import java.util.concurrent.LinkedBlockingDeque;

/**
 * @author Jezza
 */
@SideOnly(Side.CLIENT)
public final class Camera {
	private static final LinkedBlockingDeque<CameraRequest> REQUESTS = new LinkedBlockingDeque<>();
	protected static final Camera INSTANCE = new Camera();

	private CameraRequest active;

	private Camera() {
	}

	private void onActive() {
		OmnisCore.logger.info(active.modId + " activated Camera control.");
		Client.blurKeyboard();
	}

	private void release() {
		OmnisCore.logger.info(active.modId + " released Camera control.");
		active = null;
	}

	private final double[] old = new double[3];

	public void tick(RenderTickEvent event) {
		if (event.phase == Phase.START) {

		} else {

		}
	}

	public static CameraRequest request() {
		String modId = ASM.callingMod().getModId();
		CameraRequest request = new CameraRequest(modId);
		OmnisCore.logger.info(request.modId + " requested Camera control.");
		REQUESTS.add(request);
		return request;
	}

	public static class CameraRequest extends AbstractResourceRequest<CameraControl> {
		private final String modId;

		public CameraRequest(String modId) {
			this.modId = modId;
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
