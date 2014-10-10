package me.jezza.dc.common.blocks;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import me.jezza.dc.common.interfaces.IBlockInteract;
import me.jezza.dc.common.interfaces.IBlockNotifier;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public abstract class BlockAbstract extends Block {

    public BlockAbstract(Material material, String name) {
        super(material);
        setName(name);
        register(name);
    }

    public BlockAbstract setName(String name) {
        setBlockName(name);
        setBlockTextureName(name);
        return this;
    }

    public BlockAbstract register(String name) {
        GameRegistry.registerBlock(this, name);
        return this;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitVecX, float hitVecY, float hitVecZ) {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity instanceof IBlockInteract)
            return ((IBlockInteract) tileEntity).onActivated(world, x, y, z, player, side, hitVecX, hitVecY, hitVecZ);
        return false;
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLiving, ItemStack itemStack) {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity instanceof IBlockNotifier)
            ((IBlockNotifier) tileEntity).onBlockAdded(world, x, y, z);
    }

    @Override
    public void onBlockExploded(World world, int x, int y, int z, Explosion explosion) {
        onBlockRemoval(world, x, y, z);
        super.onBlockExploded(world, x, y, z, explosion);
    }

    @Override
    public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z, boolean willHarvest) {
        onBlockRemoval(world, x, y, z);
        return super.removedByPlayer(world, player, x, y, z, willHarvest);
    }

    public void onBlockRemoval(World world, int x, int y, int z) {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity instanceof IBlockNotifier)
            ((IBlockNotifier) tileEntity).onBlockRemoval(world, x, y, z);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        if (requiresTextureReg())
            blockIcon = iconRegister.registerIcon(getModIdentifier() + getTextureName());
    }

    public boolean requiresTextureReg() {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata) {
        return getTileEntity(metadata);
    }

    @Override
    public boolean hasTileEntity(int metadata) {
        return getTileEntity(metadata) != null;
    }

    /**
     * This is the one to override if you wish to create a TileEntity.
     */
    public TileEntity getTileEntity(int metadata) {
        return null;
    }

    public abstract String getModIdentifier();
}