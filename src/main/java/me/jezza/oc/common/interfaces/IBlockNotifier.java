package me.jezza.oc.common.interfaces;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.Explosion;

/**
 * Implement on a TileEntity
 */
public interface IBlockNotifier {
	/**
	 * @param explosion - the explosion in question.
	 * @return true, if the explosion was handled.
	 */
	boolean onBlockExplosion(Explosion explosion);

	/**
	 * Don't remove the block and return false, this might have unintended side-effects.
	 *
	 * @param willHarvest - if it should be harvested, or just broken.
	 * @return true - if the block should be properly removed.
	 */
	boolean removedByPlayer(boolean willHarvest);

	void onBlockAdded(EntityLivingBase entityLivingBase, ItemStack itemStack);

	void onNeighbourBlockChanged(Block block);

	void onNeighbourTileChanged(int tileX, int tileY, int tileZ);

}
