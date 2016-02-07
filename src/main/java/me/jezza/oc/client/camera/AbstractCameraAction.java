package me.jezza.oc.client.camera;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import me.jezza.oc.client.CameraAction;
import net.minecraft.entity.EntityLivingBase;

/**
 * @author Jezza
 */
@SideOnly(Side.CLIENT)
public abstract class AbstractCameraAction implements CameraAction {
	protected double[] position_data;
	protected float[] angle_data;

	@Override
	public void storeState(EntityLivingBase view) {
		position_data = new double[9];
		position_data[0] = view.posX;
		position_data[1] = view.prevPosX;
		position_data[2] = view.lastTickPosX;
		position_data[3] = view.posY;
		position_data[4] = view.prevPosY;
		position_data[5] = view.lastTickPosY;
		position_data[6] = view.posZ;
		position_data[7] = view.prevPosZ;
		position_data[8] = view.lastTickPosZ;

		angle_data = new float[6];
		angle_data[0] = view.rotationYaw;
		angle_data[1] = view.prevRotationYaw;
		angle_data[2] = view.rotationYawHead;
		angle_data[3] = view.prevRotationYawHead;
		angle_data[4] = view.rotationPitch;
		angle_data[5] = view.prevRotationPitch;
	}

	@Override
	public void restoreState(EntityLivingBase view) {
		view.posX = position_data[0];
		view.prevPosX = position_data[1];
		view.lastTickPosX = position_data[2];
		view.posY = position_data[3];
		view.prevPosY = position_data[4];
		view.lastTickPosY = position_data[5];
		view.posZ = position_data[6];
		view.prevPosZ = position_data[7];
		view.lastTickPosZ = position_data[8];
		view.rotationYaw = angle_data[0];
		view.prevRotationYaw = angle_data[1];
		view.rotationYawHead = angle_data[2];
		view.prevRotationYawHead = angle_data[3];
		view.rotationPitch = angle_data[4];
		view.prevRotationPitch = angle_data[5];
	}
}
