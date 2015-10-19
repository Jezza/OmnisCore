package me.jezza.oc.common.interfaces;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * @author Jezza
 */
public interface TooltipAdapter extends Tooltip {
	void postAddition(ItemStack stack, EntityPlayer player, boolean advancedItemTooltips);
}
