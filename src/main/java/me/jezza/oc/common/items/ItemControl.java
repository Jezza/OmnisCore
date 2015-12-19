package me.jezza.oc.common.items;

import me.jezza.oc.client.Camera;
import me.jezza.oc.client.Camera.CameraRequest;
import me.jezza.oc.common.interfaces.CameraControl;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * @author Jezza
 */
public class ItemControl extends ItemAbstract {

	private CameraRequest request;
	private CameraControl camera;

	public ItemControl() {
		super("controlItem");
		setCreativeTab(CreativeTabs.tabMisc);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
		if (world.isRemote) {
			if (request == null)
				request = Camera.request();
			if (request.acquired())
				camera = request.acquire();
		}
		return super.onItemRightClick(itemStack, world, player);
	}

	@Override
	public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		if (world.isRemote && camera != null) {
			camera.destination(x, y + 2, z);
			camera.onEnd();
//			camera.properties(LOCK_UNTIL_INTERUPTION | LOCK_UNTIL_END | LOCK_UNTIL_NOTIFY);
//			camera.speed(20);
//			camera.time(20);
//			camera.onStart(new Runnable());
//			camera.onEnd(new Runnable());
			camera.execute();
		}
		return super.onItemUseFirst(stack, player, world, x, y, z, side, hitX, hitY, hitZ);
	}
}
