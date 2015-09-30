package me.jezza.oc.common.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import me.jezza.oc.common.utils.Maths;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.List;

@Deprecated
public abstract class ItemAbstractMeta extends ItemAbstract {

    @SideOnly(Side.CLIENT)
    private IIcon[] icons;

    protected ItemAbstractMeta(String name) {
        super(name);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        List<String> names = getNames();
        for (int i = 0; i < names.size(); i++)
            list.add(new ItemStack(this, 1, i));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        List<String> names = getNames();
        icons = new IIcon[names.size()];
        for (int i = 0; i < icons.length; i++)
            icons[i] = iconRegister.registerIcon(modIdentifier + names.get(i));
    }

    @Override
    public IIcon getIconFromDamage(int damage) {
        List<String> names = getNames();
        damage = Maths.clip(damage, 0, names.size() - 1);
        return icons[damage];
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        List<String> names = getNames();
        int damage = Maths.clip(itemStack.getItemDamage(), 0, names.size() - 1);
        return "item." + names.get(damage);
    }

    public abstract List<String> getNames();
}
