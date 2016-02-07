package me.jezza.oc.client.camera;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import me.jezza.oc.common.interfaces.CameraAction;
import net.minecraft.entity.EntityLivingBase;

/**
 * @author Jezza
 */
@SideOnly(Side.CLIENT)
public abstract class AbstractCameraAction implements CameraAction {
	protected double[] positionData = new double[9];
	protected float[] angleData = new float[6];

	@Override
	public void storeState(EntityLivingBase view, float tick) {
		positionData[0] = view.posX;
		positionData[1] = view.prevPosX;
		positionData[2] = view.lastTickPosX;
		positionData[3] = view.posY;
		positionData[4] = view.prevPosY;
		positionData[5] = view.lastTickPosY;
		positionData[6] = view.posZ;
		positionData[7] = view.prevPosZ;
		positionData[8] = view.lastTickPosZ;

		angleData[0] = view.rotationYaw;
		angleData[1] = view.prevRotationYaw;
		angleData[2] = view.rotationYawHead;
		angleData[3] = view.prevRotationYawHead;
		angleData[4] = view.rotationPitch;
		angleData[5] = view.prevRotationPitch;
	}

	@Override
	public void restoreState(EntityLivingBase view, float tick) {
		view.posX = positionData[0];
		view.prevPosX = positionData[1];
		view.lastTickPosX = positionData[2];
		view.posY = positionData[3];
		view.prevPosY = positionData[4];
		view.lastTickPosY = positionData[5];
		view.posZ = positionData[6];
		view.prevPosZ = positionData[7];
		view.lastTickPosZ = positionData[8];

		view.rotationYaw = angleData[0];
		view.prevRotationYaw = angleData[1];
		view.rotationYawHead = angleData[2];
		view.prevRotationYawHead = angleData[3];
		view.rotationPitch = angleData[4];
		view.prevRotationPitch = angleData[5];
	}

	@Override
	public void cleanup(EntityLivingBase view, float tick) {
	}
}
