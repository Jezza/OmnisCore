package me.jezza.oc.common.utils.helpers;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

public class EntityHelper {

	private EntityHelper() {
		throw new IllegalStateException();
	}

	public static MovingObjectPosition getMOP(EntityLivingBase entity) {
		double distance = 4.5F;
		if (entity instanceof EntityPlayer && ((EntityPlayer) entity).capabilities.isCreativeMode)
			distance += 0.5F;
		Vec3 posVec = Vec3.createVectorHelper(entity.posX, entity.posY, entity.posZ);
		Vec3 lookVec = entity.getLook(1);
		posVec.yCoord += entity.getEyeHeight();

		lookVec = posVec.addVector(lookVec.xCoord * distance, lookVec.yCoord * distance, lookVec.zCoord * distance);
		return entity.worldObj.rayTraceBlocks(posVec, lookVec);
	}

	public static int getSideHit(EntityLivingBase entity) {
		MovingObjectPosition mouseOver = getMOP(entity);
		return mouseOver == null ? 0 : mouseOver.sideHit;
	}
}