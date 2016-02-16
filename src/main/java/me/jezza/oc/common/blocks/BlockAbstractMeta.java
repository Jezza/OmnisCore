package me.jezza.oc.common.blocks;

import java.util.List;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import me.jezza.oc.common.items.ItemBlockAbstract;
import me.jezza.oc.common.utils.helpers.StringHelper;
import me.jezza.oc.common.utils.maths.Maths;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

@Deprecated
public abstract class BlockAbstractMeta extends BlockAbstract {

	@SideOnly(Side.CLIENT)
	private IIcon[] icons;

	public BlockAbstractMeta(Material material, String name) {
		super(material, name);
	}

	@Override
	public BlockAbstract register(String name) {
		GameRegistry.registerBlock(this, getItemBlockClass(), name);
		return this;
	}

	@Override
	@SideOnly(Side.CLIENT)
	@SuppressWarnings("unchecked")
	public void getSubBlocks(Item item, CreativeTabs tab, List _list) {
		List<ItemStack> list = (List) _list;
		List<String> names = getNames();
		for (int i = 0; i < names.size(); i++)
			list.add(new ItemStack(item, 1, i));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegister) {
		List<String> names = getNames();
		icons = new IIcon[names.size()];

		StringBuilder directory = new StringBuilder(modIdentifier);
		String subDirectory = textureDirectory();
		if (StringHelper.useable(subDirectory)) {
			directory.append(subDirectory);
			if (!subDirectory.endsWith("/"))
				directory.append("/");
		}

		String registryDirectory = directory.toString();
		for (int i = 0; i < icons.length; i++)
			icons[i] = iconRegister.registerIcon(registryDirectory + names.get(Maths.clip(i, 0, names.size())));
	}

	@Override
	public int damageDropped(int damage) {
		return damage;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		return icons[meta % icons.length];
	}

	protected Class<? extends ItemBlock> getItemBlockClass() {
		return ItemBlockAbstract.class;
	}

	public String textureDirectory() {
		return null;
	}

	public abstract List<String> getNames();

}
