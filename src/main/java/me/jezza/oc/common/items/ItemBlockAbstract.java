package me.jezza.oc.common.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import me.jezza.oc.common.interfaces.Tooltip;
import me.jezza.oc.common.interfaces.TooltipAdapter;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.List;

public class ItemBlockAbstract extends ItemBlock {

	public ItemBlockAbstract(Block block) {
		super(block);
		setMaxDamage(0);
		setHasSubtypes(true);
	}

	public Block getBlock() {
		return field_150939_a;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int damage) {
		return getBlock().getIcon(2, damage);
	}

	@Override
	public int getMetadata(int meta) {
		return meta;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public final void addInformation(ItemStack stack, EntityPlayer player, List _list, boolean advancedItemTooltips) {
		@SuppressWarnings("unchecked")
		TooltipAdapter adapter = createTooltipAdapter((List<String>) _list);
		addInformation(stack, player, adapter, advancedItemTooltips);
		adapter.postAddition(stack, player, advancedItemTooltips);
	}

	protected TooltipAdapter createTooltipAdapter(List<String> tooltip) {
		return new ItemTooltipInformation(tooltip);
	}

	protected void addInformation(ItemStack stack, EntityPlayer player, Tooltip tooltip, boolean advancedItemTooltips) {
	}
}
