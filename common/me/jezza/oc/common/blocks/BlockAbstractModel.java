package me.jezza.oc.common.blocks;

import net.minecraft.block.material.Material;

public abstract class BlockAbstractModel extends BlockAbstract {

    public BlockAbstractModel(Material material, String name) {
        super(material, name);
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public int getRenderType() {
        return -1;
    }

    @Override
    public boolean requiresTextureReg() {
        return false;
    }
}
