package me.jezza.oc.common.interfaces;

import net.minecraft.world.World;

/**
 * Implement on a TileEntity
 */
public interface IBlockNotifier {

    public void onBlockRemoval(World world, int x, int y, int z);

    public void onBlockAdded(World world, int x, int y, int z);

}
