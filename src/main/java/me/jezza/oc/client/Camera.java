package me.jezza.oc.client;

import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.RenderTickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import me.jezza.oc.OmnisCore;
import me.jezza.oc.client.lib.AbstractResourceRequest;
import me.jezza.oc.common.interfaces.CameraAction;
import me.jezza.oc.common.utils.reflect.ASM;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;

import java.util.concurrent.LinkedBlockingDeque;

/**
 * @author Jezza
 */
@SideOnly(Side.CLIENT)
public final class Camera {
	private static final LinkedBlockingDeque<CameraRequest> REQUESTS = new LinkedBlockingDeque<>();
	protected static final LinkedBlockingDeque<CameraAction> ACTIONS = new LinkedBlockingDeque<>();
	protected static final Camera INSTANCE = new Camera();

	private CameraRequest activeRequest;
	private CameraAction activeAction;
	private boolean stateStored = false;
	private boolean actionFinished = false;

	private Camera() {
	}

	private void onActive() {
		OmnisCore.logger.info(activeRequest.modId + " activated Camera control.");
		Client.blurKeyboard();
	}

	private void release() {
		OmnisCore.logger.info(activeRequest.modId + " released Camera control.");
		activeRequest = null;
	}

	protected void tick(ClientTickEvent event) {
		if (event.phase != Phase.START)
			return;
		if (activeRequest != null) {
			if (activeRequest.cancelled()) {
				activeRequest.release();
				activeRequest = null;
			}
			return;
		}
		if (REQUESTS.isEmpty())
			return;
		while ((activeRequest == null || activeRequest.cancelled()) && !REQUESTS.isEmpty())
			activeRequest = REQUESTS.pollFirst();
		if (activeRequest == null)
			return;
		if (activeRequest.cancelled()) {
			activeRequest = null;
			return;
		}
		OmnisCore.logger.info(activeRequest.modId + " acquired Camera control.");
		activeRequest.acquired(new CameraControl());
	}

	public void tick(RenderTickEvent event) {
		EntityLivingBase view = Minecraft.getMinecraft().renderViewEntity;
		if (view == null) {
			activeAction = null;
			ACTIONS.clear();
			return;
		}
		if (activeAction != null) {
			if (!stateStored) {
				stateStored = true;
				activeAction.storeState(view, event.renderTickTime);
			}
			if (event.phase == Phase.START) {
				actionFinished = activeAction.cameraRender(view, event.renderTickTime);
			} else if (actionFinished) {
				activeAction.restoreState(view, event.renderTickTime);
				activeAction = null;
			}
			return;
		}
		if (ACTIONS.isEmpty())
			return;
		while (activeAction == null && !ACTIONS.isEmpty())
			activeAction = ACTIONS.pollFirst();
		// Just make sure it's been reset.
		actionFinished = false;
		stateStored = false;
	}

	public static void queue(CameraAction action) {
		String modId = ASM.callingMod().getModId();
		OmnisCore.logger.info(modId + " queued a Camera Action.");
		ACTIONS.offerLast(action);
	}

	public static CameraRequest request() {
		// TODO Should probably clear all actions, and then stop any more actions being queued.
		throw new UnsupportedOperationException("Not Yet Implemented!");
//		String modId = ASM.callingMod().getModId();
//		CameraRequest request = new CameraRequest(modId);
//		OmnisCore.logger.info(request.modId + " requested Camera control.");
//		REQUESTS.offerLast(request);
//		return request;
	}

	public static CameraControl control() {
		throw new UnsupportedOperationException("Not Yet Implemented!");
		// return new CameraControl();
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
		protected void onRelease() {
			INSTANCE.release();
		}

		@Override
		protected void acquired(CameraControl resource) {
			super.acquired(resource);
		}
	}
}
