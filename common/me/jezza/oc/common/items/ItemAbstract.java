package me.jezza.oc.common.items;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import me.jezza.oc.client.ClientUtil;
import me.jezza.oc.common.utils.MovingObjectPositionHelper;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import java.util.List;

public abstract class ItemAbstract extends Item {
    private String donator = "";
    private boolean isDonatorItem = false;

    private boolean textureReg = true;
    private boolean notifyRightClick = false;
    private boolean notifyLeftClick = false;

    public ItemAbstract(String name) {
        setName(name);
        register(name);
    }

    public ItemAbstract setName(String name) {
        setUnlocalizedName(name);
        setTextureName(name);
        return this;
    }

    public ItemAbstract register(String name) {
        GameRegistry.registerItem(this, name);
        return this;
    }

    public ItemAbstract setTextureless() {
        this.textureReg = false;
        return this;
    }

    public ItemAbstract setRightClickable() {
        notifyRightClick = true;
        return this;
    }

    public ItemAbstract setLeftClickable() {
        notifyLeftClick = true;
        return this;
    }

    // TODO Move this to ES2
    public ItemAbstract setDonatorItem(String donator) {
        this.donator = donator;
        isDonatorItem = true;
        return this;
    }

    public ItemAbstract setShapelessRecipe(Object... items) {
        return setShapelessRecipe(1, items);
    }

    public ItemAbstract setShapelessRecipe(int resultSize, Object... items) {
        return setShapelessRecipe(resultSize, 0, items);
    }

    public ItemAbstract setShapelessRecipe(int resultSize, int meta, Object... items) {
        CraftingManager.getInstance().addShapelessRecipe(new ItemStack(this, resultSize, meta), items);
        return this;
    }

    @SideOnly(Side.CLIENT)
    public boolean isDonator() {
        return isDonatorItem && ClientUtil.isPlayer(donator);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        if (notifyRightClick) {
            MovingObjectPosition mop = MovingObjectPositionHelper.getCurrentMovingObjectPosition(player);
            return onItemRightClick(itemStack, world, player, mop);
        }
        return super.onItemRightClick(itemStack, world, player);
    }

    @Override
    public boolean onEntitySwing(EntityLivingBase entity, ItemStack itemStack) {
        if (notifyLeftClick) {
            MovingObjectPosition mop = MovingObjectPositionHelper.getCurrentMovingObjectPosition(entity);
            return onItemLeftClick(itemStack, entity.worldObj, entity, mop);
        }
        return super.onEntitySwing(entity, itemStack);
    }

    /**
     * Called whenever the item is right clicked.
     */
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player, MovingObjectPosition mop) {
        return itemStack;
    }

    /**
     * Return true to stop all further processing.
     */
    public boolean onItemLeftClick(ItemStack itemStack, World world, EntityLivingBase player, MovingObjectPosition mop) {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        if (textureReg)
            itemIcon = iconRegister.registerIcon(getModIdentifier() + getIconString());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
        ItemInformation information = new ItemInformation();
        addInformation(stack, player, information);
        if (isDonator())
            information.addShiftList("This item was made thanks to you.");
        information.addToList(list);
    }

    protected void addInformation(ItemStack stack, EntityPlayer player, ItemInformation information) {
    }

    public abstract String getModIdentifier();
}