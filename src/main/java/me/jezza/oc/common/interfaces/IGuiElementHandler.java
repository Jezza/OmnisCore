package me.jezza.oc.common.interfaces;

import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.world.World;

/**
 * @author Jezza
 */
public interface IGuiElementHandler extends IGuiHandler {

	@Override
	Container getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z);

	@Override
	GuiScreen getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z);
}
