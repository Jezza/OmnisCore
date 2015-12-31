package me.jezza.oc.common.items;

import me.jezza.oc.client.Camera;
import me.jezza.oc.client.Camera.CameraRequest;
import me.jezza.oc.client.CameraControl;
import me.jezza.oc.common.utils.RayTrace;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
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
			if (camera == null && request.acquired())
				camera = request.acquire();
			MovingObjectPosition mop = RayTrace.create(player).distance(64D).trace();
			player.addChatComponentMessage(new ChatComponentText(mop.toString()));
			if (camera != null && mop.typeOfHit == MovingObjectType.BLOCK) {
				camera.destination(mop.blockX, mop.blockY + 1, mop.blockZ).execute();
				request.release();
				request = null;
				camera = null;
			}
		}
		return super.onItemRightClick(itemStack, world, player);
	}
}
