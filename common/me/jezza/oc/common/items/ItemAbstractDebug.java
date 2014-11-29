package me.jezza.oc.common.items;

import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

import java.util.ArrayList;

public abstract class ItemAbstractDebug extends ItemAbstract {

    private int debugMode = 0;

    public ItemAbstractDebug(String name) {
        super(name);
        setMaxStackSize(1);
    }

    @Override
    public ItemAbstract register(String name) {
//        if (DeusCore.isDebugMode())
        return super.register(name);
//        return this;
    }

    public int getDebugMode() {
        return debugMode;
    }

    private String getDebugString() {
        return getCurrentDebugString() + " Mode";
    }

    public String getCurrentDebugString() {
        return getDebugList().get(debugMode);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
//        if (DeusCore.isDebugMode()) {
        if (player.isSneaking()) {
            if (++debugMode == getDebugList().size())
                debugMode = 0;
            player.addChatComponentMessage(new ChatComponentText(getDebugString()));
        }
//        }
        return itemStack;
    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int sideHit, float hitVecX, float hitVecY, float hitVecZ) {
        return onItemDebugUse(itemStack, player, world, x, y, z, sideHit, hitVecX, hitVecY, hitVecZ, world.isRemote ? Side.CLIENT : Side.SERVER);
    }

    @Override
    public boolean onItemUseFirst(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int sideHit, float hitVecX, float hitVecY, float hitVecZ) {
        return onItemDebugUseFirst(itemStack, player, world, x, y, z, sideHit, hitVecX, hitVecY, hitVecZ, world.isRemote ? Side.CLIENT : Side.SERVER);
    }

    @Override
    protected void addInformation(ItemStack stack, EntityPlayer player, ItemInformation information) {
        information.addInfoList("Debug Mode: " + debugMode);
        information.addInfoList(getDebugString());
    }

    public void addChatMessage(EntityPlayer player, String string) {
        player.addChatComponentMessage(new ChatComponentText(string));
    }

    public abstract ArrayList<String> getDebugList();

    /**
     * Called after onItemDebugUseFirst.
     * Return true to stop all processing.
     */
    public abstract boolean onItemDebugUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int sideHit, float hitVecX, float hitVecY, float hitVecZ, Side side);

    /**
     * Called before anything else on the item when used.
     * Return true to stop processing.
     */
    public abstract boolean onItemDebugUseFirst(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int sideHit, float hitVecX, float hitVecY, float hitVecZ, Side side);
}