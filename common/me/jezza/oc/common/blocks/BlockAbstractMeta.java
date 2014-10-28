package me.jezza.oc.common.blocks;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import me.jezza.oc.common.core.utils.MathHelper;
import me.jezza.oc.common.items.ItemBlockAbstract;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.List;

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
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
        List<String> names = getNames();
        for (int i = 0; i < names.size(); i++)
            list.add(new ItemStack(item, 1, i));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        List<String> names = getNames();
        icons = new IIcon[names.size()];

        StringBuilder registryBuilder = new StringBuilder(getModIdentifier());
        if (usesTextureSubDirectory())
            registryBuilder.append(getTextureSubDirectory() + "/");

        String registerString = registryBuilder.toString();
        for (int i = 0; i < icons.length; i++)
            icons[i] = iconRegister.registerIcon(registerString + names.get(MathHelper.clipInt(i, names.size())));
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

    public boolean usesTextureSubDirectory() {
        return !getTextureSubDirectory().equals("");
    }

    public String getTextureSubDirectory() {
        return "";
    }

    public abstract List<String> getNames();

}
