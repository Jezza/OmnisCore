package me.jezza.oc.common.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import me.jezza.oc.client.ClientUtil;
import me.jezza.oc.common.interfaces.Tooltip;
import me.jezza.oc.common.interfaces.TooltipPopulator;
import me.jezza.oc.common.utils.helpers.StringHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.lwjgl.input.Keyboard;

import java.util.List;

@SideOnly(Side.CLIENT)
public class ItemTooltipInformation implements TooltipPopulator {
	private final List<String> tooltip;

	public ItemTooltipInformation(List<String> tooltip) {
		this.tooltip = tooltip;
	}

	@Override
	public Tooltip defaultInfo() {
		add("info.tooltip.default.shift", TRANSLATE | COLOUR);
		return this;
	}

	@Override
	public Tooltip add(CharSequence sequence) {
		tooltip.add(sequence.toString());
		return this;
	}

	@Override
	public Tooltip add(Iterable<? extends CharSequence> sequences) {
		for (CharSequence sequence : sequences)
			tooltip.add(sequence.toString());
		return this;
	}

	@Override
	public Tooltip add(CharSequence sequence, int flags) {
		String value = sequence.toString();
		if ((flags & TRANSLATE) != 0)
			value = StringHelper.translate(value);
		if ((flags & COLOUR) != 0)
			value = StringHelper.formatColour(value);
		if ((flags & WRAP) != 0) {
			tooltip.addAll(StringHelper.wrap(value, flags & WRAP_MASK));
		} else {
			tooltip.add(value);
		}
		return this;
	}

	@Override
	public Tooltip add(Iterable<? extends CharSequence> sequences, int flags) {
		boolean translate = (flags & TRANSLATE) != 0;
		boolean wrap = (flags & WRAP) != 0;
		boolean colour = (flags & COLOUR) != 0;
		int wrapLength = flags & WRAP_MASK;
		for (CharSequence sequence : sequences) {
			String value = sequence.toString();
			if (translate)
				value = StringHelper.translate(value);
			if (colour)
				value = StringHelper.formatColour(value);
			if (wrap) {
				tooltip.addAll(StringHelper.wrap(value, wrapLength));
			} else {
				tooltip.add(value);
			}
		}
		return this;
	}

	@Override
	public boolean shift() {
		return ClientUtil.hasPressedShift();
	}

	@Override
	public boolean shift(int key) {
		return ClientUtil.hasPressedShift() && Keyboard.isKeyDown(key);
	}

	@Override
	public boolean ctrl() {
		return ClientUtil.hasPressedCtrl();
	}

	@Override
	public boolean ctrl(int key) {
		return ClientUtil.hasPressedCtrl() && Keyboard.isKeyDown(key);
	}

	@Override
	public boolean alt() {
		return ClientUtil.hasPressedAlt();
	}

	@Override
	public boolean alt(int key) {
		return ClientUtil.hasPressedAlt() && Keyboard.isKeyDown(key);
	}

	@Override
	public boolean pressed(int key) {
		return Keyboard.isKeyDown(key);
	}

	@Override
	public void postAddition(ItemStack stack, EntityPlayer player, boolean advancedItemTooltips) {
	}
}