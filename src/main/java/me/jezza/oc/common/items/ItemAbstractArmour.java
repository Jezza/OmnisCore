package me.jezza.oc.common.items;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import me.jezza.oc.common.interfaces.Tooltip;
import me.jezza.oc.common.interfaces.TooltipAdapter;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

import java.util.List;

public abstract class ItemAbstractArmour extends ItemArmor {
	private String textureLocation;
	private boolean textureReg;
	private int slot;
	public final String modIdentifier;

	public ItemAbstractArmour(ArmorMaterial armorMaterial, ArmourRenderIndex renderIndex, ArmourSlotIndex armourIndex, String name, String textureLocation) {
		this(armorMaterial, renderIndex.ordinal(), armourIndex.ordinal(), name, textureLocation);
	}

	public ItemAbstractArmour(ArmorMaterial armorMaterial, int renderIndex, int armourIndex, String name, String textureLocation) {
		super(armorMaterial, renderIndex, armourIndex);
		modIdentifier = Loader.instance().activeModContainer().getModId() + ":";
		slot = armourIndex;
		this.textureLocation = textureLocation;
		setMaxDamage(0);
		setName(name);
		register(name);
	}

	public void setName(String name) {
		setUnlocalizedName(name);
		setTextureName(name);
	}

	public ItemAbstractArmour register(String name) {
		GameRegistry.registerItem(this, name);
		return this;
	}

	public ItemAbstractArmour setTextureReg(boolean textureReg) {
		this.textureReg = textureReg;
		return this;
	}

	@Override
	public boolean isValidArmor(ItemStack stack, int armorType, Entity entity) {
		return armorType == slot;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean requiresMultipleRenderPasses() {
		return false;
	}

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
		return textureLocation;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister register) {
		if (textureReg)
			itemIcon = register.registerIcon(modIdentifier + getIconString());
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

	public enum ArmourSlotIndex {
		HEAD, CHEST_PLATE, LEGGINGS, BOOTS
	}

	public enum ArmourRenderIndex {
		CLOTH, CHAIN, IRON, DIAMOND, GOLD
	}
}
