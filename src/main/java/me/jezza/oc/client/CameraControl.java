package me.jezza.oc.client;

import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.RenderTickEvent;
import me.jezza.oc.OmnisCore;
import me.jezza.oc.common.interfaces.CameraAction;
import me.jezza.oc.common.interfaces.Resource;
import net.minecraft.entity.EntityLivingBase;

/**
 * @author Jezza
 */
public final class CameraControl implements Resource {
	public static final int LOCK = 0b1000000000000000000;
	public static final int TIME_MASK = 0b111111111111111111;
	public static final int TIME_MAX = TIME_MASK;

	private double x, y, z;

	private boolean valid = true;

	protected CameraControl() {
	}

	public CameraControl destination(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	}

	public CameraControl lookAt(double x, double y, double z) {
		return this;
	}

	public CameraControl execute() {
		if (!valid)
			throw new IllegalStateException("Camera Control has already been released, you can't do shit anymore.");
		Camera.internalQueue(new CameraAction() {

			private double oldX, oldY, oldZ;

			private int ticks = 0;

			@Override
			public boolean cameraRender(RenderTickEvent event, EntityLivingBase view) {
				if (event.phase == Phase.START) {
					oldX = view.posX;
					oldY = view.posY;
					oldZ = view.posZ;
					view.posX = view.prevPosX = view.lastTickPosX = x;
					view.posY = view.prevPosY = view.lastTickPosY = y;
					view.posZ = view.prevPosZ = view.lastTickPosZ = z;
				} else {
					view.posX = view.prevPosX = view.lastTickPosX = oldX;
					view.posY = view.prevPosY = view.lastTickPosY = oldY;
					view.posZ = view.prevPosZ = view.lastTickPosZ = oldZ;
				}
				return ticks++ >= 500;
			}
		});
		OmnisCore.logger.info("Executing action, {}, {}, {}", x, y, z);
		return this;
	}

	public boolean valid() {
		return valid;
	}

	@Override
	public void invalidate() {
		valid = false;
	}
}
