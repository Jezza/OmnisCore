package me.jezza.oc.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import me.jezza.oc.OmnisCore;
import me.jezza.oc.common.interfaces.Resource;

/**
 * @author Jezza
 */
@SideOnly(Side.CLIENT)
public final class CameraControl implements Resource {
	public static final int LOCK = 0b1000000000000000000;
	public static final int TIME_MASK = 0b111111111111111111;
	public static final int TIME_MAX = TIME_MASK;

	private double x, y, z;

	private boolean valid = true;

	public CameraControl() {
		throw new UnsupportedOperationException("Not Yet Implemented");
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

	public CameraControl queue() {
		if (!valid)
			throw new IllegalStateException("Camera Control has already been released, you can't do shit anymore.");
//		Camera.internalQueue(new CameraAction() {
//
//			private int ticks = 0;
//
//			@Override
//			public boolean cameraRender(EntityLivingBase view, float tick) {
//				view.posX = view.prevPosX = view.lastTickPosX = x;
//				view.posY = view.prevPosY = view.lastTickPosY = y;
//				view.posZ = view.prevPosZ = view.lastTickPosZ = z;
//				return ticks++ >= 500;
//			}
//		});
		OmnisCore.logger.info("Executing action, {}, {}, {}", x, y, z);
		reset();
		return this;
	}

	public void reset() {
	}

	public boolean valid() {
		return valid;
	}

	@Override
	public void invalidate() {
		valid = false;
	}
}
