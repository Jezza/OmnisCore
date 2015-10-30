package me.jezza.oc.common.utils.helpers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;

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

	@Deprecated
	public static MovingObjectPosition getComplexMOP(EntityLivingBase entity) {
		double posX = entity.posX;
		double posY = entity.posY;
		double posZ = entity.posZ;
		Vec3 lookVec = entity.getLook(1);
		double motionX = lookVec.xCoord * 64; // entity.motionX;
		double motionY = lookVec.yCoord * 64; // entity.motionY;
		double motionZ = lookVec.zCoord * 64; // entity.motionZ;
		Vec3 posVector = Vec3.createVectorHelper(posX, posY, posZ);
		Vec3 motionVector = Vec3.createVectorHelper(posX + motionX, posY + motionY, posZ + motionZ);
		if (!entity.worldObj.isRemote)
			motionVector.yCoord += entity.getEyeHeight();
		MovingObjectPosition mop = entity.worldObj.rayTraceBlocks(posVector, motionVector);
		System.out.println(posVector);
		System.out.println(motionVector);
		System.out.println(mop);
		if (entity.worldObj.isRemote)
			return mop;

		posVector = Vec3.createVectorHelper(posX, posY, posZ);
		if (mop != null) {
			motionVector = Vec3.createVectorHelper(mop.hitVec.xCoord, mop.hitVec.yCoord, mop.hitVec.zCoord);
		} else {
			motionVector = Vec3.createVectorHelper(posX + motionX, posY + motionY, posZ + motionZ);
			motionVector.yCoord += entity.getEyeHeight();
		}

		Entity hit = null;
		@SuppressWarnings("unchecked")
		List<Entity> list = (List<Entity>) entity.worldObj.getEntitiesWithinAABBExcludingEntity(entity, entity.boundingBox.addCoord(motionX, motionY, motionZ).expand(1.0D, 1.0D, 1.0D));
		double d0 = 0.0D;
		for (int j = 0; j < list.size(); ++j) {
			Entity potential = list.get(j);
			if (potential.canBeCollidedWith()) {
				AxisAlignedBB axisalignedbb = potential.boundingBox.expand(0.3D, 0.3D, 0.3D);
				MovingObjectPosition intercept = axisalignedbb.calculateIntercept(posVector, motionVector);
				if (intercept != null) {
					double d1 = posVector.distanceTo(intercept.hitVec);
					if (d1 < d0 || d0 == 0.0D) {
						hit = potential;
						d0 = d1;
					}
				}
			}
		}
		return hit != null ? new MovingObjectPosition(hit) : null;
	}

	public static int getSideHit(EntityLivingBase entity) {
		MovingObjectPosition mouseOver = getMOP(entity);
		return mouseOver == null ? 0 : mouseOver.sideHit;
	}

	public static ForgeDirection horizontalDirection(EntityLivingBase entityLivingBase) {
		int facing = MathHelper.floor_double(entityLivingBase.rotationYaw * 4.0D / 360.0D + 2.5D) & 3;
		switch (facing) {
			case 0:
				return ForgeDirection.NORTH;
			case 1:
				return ForgeDirection.EAST;
			case 2:
				return ForgeDirection.SOUTH;
			case 3:
				return ForgeDirection.WEST;
			default:
				return ForgeDirection.UNKNOWN;
		}
	}
}