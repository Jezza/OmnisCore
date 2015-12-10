package me.jezza.oc.common.utils.helpers;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * @author Jezza
 */
public enum ItemStackHelper {
	;

	public static NBTTagCompound tag(ItemStack itemStack) {
		return tag(itemStack, true);
	}

	public static NBTTagCompound tag(ItemStack itemStack, boolean create) {
		NBTTagCompound tag = itemStack.getTagCompound();
		if (tag == null && create) {
			tag = new NBTTagCompound();
			itemStack.setTagCompound(tag);
		}
		return tag;
	}

	public static NBTTagCompound tag(ItemStack itemStack, String key) {
		return tag(tag(itemStack, true), key, true);
	}

	public static NBTTagCompound tag(ItemStack itemStack, String key, boolean create) {
		NBTTagCompound tag = tag(itemStack, create);
		return tag != null ? tag(tag, key, create) : null;
	}

	public static NBTTagCompound tag(NBTTagCompound tag, String key) {
		return tag(tag, key, true);
	}

	public static NBTTagCompound tag(NBTTagCompound tag, String key, boolean create) {
		if (tag == null)
			return null;
		NBTTagCompound sub = tag.getCompoundTag(key);
		if (sub == null && create) {
			sub = new NBTTagCompound();
			tag.setTag(key, sub);
		}
		return sub;
	}
}
