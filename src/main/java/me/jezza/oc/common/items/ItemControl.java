package me.jezza.oc.common.items;

import me.jezza.oc.OmnisCore;
import me.jezza.oc.client.Mouse;
import me.jezza.oc.common.interfaces.MouseAdapter;
import me.jezza.oc.common.interfaces.Request;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * @author Jezza
 */
public class ItemControl extends ItemAbstract implements MouseAdapter {

	private Request request;

	public ItemControl() {
		super("controlItem");
		setCreativeTab(CreativeTabs.tabMisc);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
		if (world.isRemote) {
			if (request == null)
				request = Mouse.request(this);
			if (request.acquired())
				request.acquire();
		}
		return super.onItemRightClick(itemStack, world, player);
	}

	@Override
	public void onClick(int x, int y, int key) {
		OmnisCore.logger.info("X:{}, Y:{}, Key:{}", x, y, key);
		if (key == 1 && request != null) {
			request.release();
			request = null;
		}
	}

	@Override
	public void onRelease(int mouseX, int mouseY, int key) {

	}

	@Override
	public void mouseChange(int mouseX, int mouseY) {

	}
}
