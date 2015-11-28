package me.jezza.oc.common.items;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import me.jezza.oc.common.interfaces.Tooltip;
import me.jezza.oc.common.interfaces.TooltipPopulator;
import me.jezza.oc.common.utils.ASM;
import me.jezza.oc.common.utils.RayTrace;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.util.MovingObjectPosition;

import java.util.List;

public abstract class ItemAbstract extends Item {
	protected boolean textureReg = true;
	protected boolean effect = false;
	public final String modIdentifier;

	public ItemAbstract(String name) {
		modIdentifier = ASM.findOwner(getClass()).getModId() + ':';
		setName(name);
		register(name);
	}

	protected ItemAbstract setName(String name) {
		setUnlocalizedName(name);
		setTextureName(name);
		return this;
	}

	protected ItemAbstract register(String name) {
		GameRegistry.registerItem(this, name);
		return this;
	}

	protected ItemAbstract textureless(boolean textureless) {
		textureReg = !textureless;
		return this;
	}

	protected ItemAbstract effect(boolean effect) {
		this.effect = effect;
		return this;
	}

	public ItemAbstract setShapelessRecipe(Object... items) {
		return setShapelessRecipe(1, 0, items);
	}

	public ItemAbstract setShapelessRecipe(int resultSize, Object... items) {
		return setShapelessRecipe(resultSize, 0, items);
	}

	public ItemAbstract setShapelessRecipe(int resultSize, int damage, Object... items) {
		CraftingManager.getInstance().addShapelessRecipe(new ItemStack(this, resultSize, damage), items);
		return this;
	}

	public MovingObjectPosition rayTrace(EntityLivingBase entity) {
		return RayTrace.of(entity);
	}

	@Override
	@Deprecated
	@SideOnly(Side.CLIENT)
	public boolean hasEffect(ItemStack itemStack) {
		return effect || super.hasEffect(itemStack);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iconRegister) {
		if (textureReg)
			itemIcon = iconRegister.registerIcon(modIdentifier + getIconString());
	}

	@Override
	@SideOnly(Side.CLIENT)
	public final void addInformation(ItemStack stack, EntityPlayer player, List _list, boolean advancedItemTooltips) {
		@SuppressWarnings("unchecked")
		TooltipPopulator populator = createTooltipPopulator((List<String>) _list);
		addInformation(stack, player, populator, advancedItemTooltips);
		populator.postAddition(stack, player, advancedItemTooltips);
	}

	protected TooltipPopulator createTooltipPopulator(List<String> tooltip) {
		return new ItemTooltipInformation(tooltip);
	}

	protected void addInformation(ItemStack stack, EntityPlayer player, Tooltip tooltip, boolean advancedItemTooltips) {
	}
}