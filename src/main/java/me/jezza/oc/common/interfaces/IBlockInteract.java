package me.jezza.oc.common.interfaces;

import net.minecraft.entity.player.EntityPlayer;

/**
 * Implement on a TileEntity
 */
public interface IBlockInteract {
	boolean onActivated(EntityPlayer player, int side, float hitVecX, float hitVecY, float hitVecZ);
}