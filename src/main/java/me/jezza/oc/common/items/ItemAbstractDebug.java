package me.jezza.oc.common.items;

import me.jezza.oc.common.interfaces.Tooltip;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

import java.util.ArrayList;

/**
 * TODO: Revisit
 */
public abstract class ItemAbstractDebug extends ItemAbstract {
	private int debugMode = 0;

	public ItemAbstractDebug(String name) {
		super(name);
		setMaxStackSize(1);
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
		if (player.isSneaking()) {
			if (++debugMode == getDebugList().size())
				debugMode = 0;
			player.addChatComponentMessage(new ChatComponentText(getDebugString()));
		}
		return itemStack;
	}

	@Override
	public abstract boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int sideHit, float hitVecX, float hitVecY, float hitVecZ);

	@Override
	public abstract boolean onItemUseFirst(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int sideHit, float hitVecX, float hitVecY, float hitVecZ);

	@Override
	protected void addInformation(ItemStack stack, EntityPlayer player, Tooltip tooltip, boolean advancedItemTooltips) {
		tooltip.add("Debug Mode: " + debugMode);
		tooltip.add(getDebugString());
	}

	public void addChatMessage(EntityPlayer player, String string) {
		player.addChatComponentMessage(new ChatComponentText(string));
	}

	public abstract ArrayList<String> getDebugList();
}