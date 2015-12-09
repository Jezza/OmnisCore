package me.jezza.oc.common.utils.helpers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import static org.apache.http.util.Args.notNull;

public class EntityHelper {

	private EntityHelper() {
		throw new IllegalStateException();
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

	public static String name(Entity entity) {
		notNull(entity, "entity");
		String value = EntityList.getEntityString(entity);
		return value != null ? StringHelper.translate("entity." + value + ".name") : "Unknown";
	}
}