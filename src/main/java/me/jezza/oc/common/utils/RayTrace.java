package me.jezza.oc.common.utils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

import java.util.List;

/**
 * @author Jezza
 */
public class RayTrace {
	private final EntityLivingBase entity;
	private double distance;

	protected RayTrace(final EntityLivingBase entity) {
		this.entity = entity;
		distance = entity instanceof EntityPlayer && ((EntityPlayer) entity).capabilities.isCreativeMode ? 5.0D : 4.5D;
	}

	protected RayTrace(final EntityLivingBase entity, double distance) {
		this.entity = entity;
		distance(distance);
	}

	public RayTrace distance(double distance) {
		this.distance = distance < 0.0D ? Double.MAX_VALUE : distance >= 3.0D ? distance : 3.0D;
		return this;
	}

	public double distance() {
		return distance;
	}

	protected Vec3 createPositionVector() {
		Vec3 pos = Vec3.createVectorHelper(entity.posX, entity.posY, entity.posZ);
		if (!entity.worldObj.isRemote)
			pos.yCoord += entity.getEyeHeight();
		return pos;
	}

	protected Vec3 createMagnitudeVector(Vec3 position) {
		Vec3 look = entity.getLookVec();
		return position.addVector(look.xCoord * distance, look.yCoord * distance, look.zCoord * distance);
	}

	public MovingObjectPosition trace() {
		Vec3 pos = createPositionVector();
		Vec3 magnitude = createMagnitudeVector(pos);
		MovingObjectPosition mop = entity.worldObj.rayTraceBlocks(pos, magnitude, false);

		// Reset position vector, because something changes it.
		pos = createPositionVector();
		Vec3 look = entity.getLookVec();

		Entity hit = null;
		@SuppressWarnings("unchecked")
		List<Entity> list = (List<Entity>) entity.worldObj.getEntitiesWithinAABBExcludingEntity(entity, entity.boundingBox.addCoord(look.xCoord * distance, look.yCoord * distance, look.zCoord * distance).expand(1.0D, 1.0D, 1.0D));
		double smallest = 0.0D;
		for (int j = list.size() - 1; j >= 0; --j) {
			Entity potential = list.get(j);
			if (potential.canBeCollidedWith()) {
				double borderSize = potential.getCollisionBorderSize();
				AxisAlignedBB boundingBox = potential.boundingBox.expand(borderSize, borderSize, borderSize);
				MovingObjectPosition intercept = boundingBox.calculateIntercept(pos, magnitude);
				if (intercept != null) {
					double distanceTo = pos.distanceTo(intercept.hitVec);
					if (distanceTo < smallest || smallest == 0.0D) {
						hit = potential;
						smallest = distanceTo;
					}
				}
			}
		}
		if (hit != null)
			return new MovingObjectPosition(hit);
		if (mop != null)
			return mop;
		return new MovingObjectPosition(0, 0, 0, -1, Vec3.createVectorHelper(0, 0, 0), false);
	}

	public static RayTrace create(EntityLivingBase entity) {
		return new RayTrace(entity);
	}

	public static RayTrace create(EntityLivingBase entity, double distance) {
		return new RayTrace(entity, distance);
	}

	public static MovingObjectPosition of(EntityLivingBase entity) {
		return create(entity).trace();
	}

	public static MovingObjectPosition of(EntityLivingBase entity, double distance) {
		return create(entity, distance).trace();
	}
}
