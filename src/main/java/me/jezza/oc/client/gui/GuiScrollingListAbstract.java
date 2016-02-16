package me.jezza.oc.client.gui;

import java.util.List;

import cpw.mods.fml.client.GuiScrollingList;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;

/**
 * @author Jezza
 */
public abstract class GuiScrollingListAbstract<T> extends GuiScrollingList {
	protected final GuiScreen parent;
	protected List<T> data;

	private int selectedIndex = -1;

	public GuiScrollingListAbstract(GuiScreen parent, List<T> data, int listWidth) {
		this(parent, data, listWidth, parent.height, 32, parent.height - 66 + 4, 10, 35);
	}

	public GuiScrollingListAbstract(GuiScreen parent, List<T> data, int width, int height, int top, int bottom, int left, int entryHeight) {
		super(parent.mc, width, height, top, bottom, left, entryHeight);
		this.parent = parent;
		this.data = data;
	}

	protected void offset(boolean enable, int offset) {
		func_27259_a(enable, offset);
	}

	@Override
	protected int getSize() {
		return data.size();
	}

	@Override
	protected final void elementClicked(int index, boolean doubleClicked) {
		select(index);
		elementClicked(index, item(), doubleClicked);
	}

	protected abstract void elementClicked(int index, T element, boolean doubleClicked);

	protected T item() {
		return selectedIndex >= 0 ? data.get(selectedIndex) : null;
	}

	protected void select(int index) {
		selectedIndex = index;
	}

	protected int currentIndex() {
		return selectedIndex;
	}

	@Override
	protected boolean isSelected(int index) {
		return currentIndex() == index;
	}

	@Override
	protected void drawBackground() {
	}

	@Override
	protected final void drawSlot(int listIndex, int right, int top, int left, Tessellator instance) {
		drawSlot(listIndex, data.get(listIndex), right, top, left, instance, parent.mc.fontRenderer);
	}

	protected abstract void drawSlot(int listIndex, T item, int right, int top, int left, Tessellator instance, FontRenderer fontRenderer);
}
