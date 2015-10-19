package me.jezza.oc.common.interfaces;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public interface ITileProvider extends ITileEntityProvider {

    @Override
    TileEntity createNewTileEntity(World world, int meta);



}
