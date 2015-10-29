package me.jezza.oc.common.blocks;

import net.minecraft.block.material.Material;

public abstract class BlockAbstractModel extends BlockAbstractGlass {

	public BlockAbstractModel(Material material, String name) {
		super(material, name);
		textureless(true);
	}

	@Override
	public int getRenderType() {
		return -1;
	}
}
