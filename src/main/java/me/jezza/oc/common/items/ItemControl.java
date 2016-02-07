package me.jezza.oc.common.items;

import me.jezza.oc.client.Camera;
import me.jezza.oc.client.camera.AbstractCameraAction;
import me.jezza.oc.common.utils.RayTrace;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
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
	private static final int TOTAL_FRAMES = 60 * 5;
	private boolean queued = false;

	public ItemControl() {
		super("controlItem");
		setCreativeTab(CreativeTabs.tabMisc);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
		if (world.isRemote) {
			if (!queued) {
				final MovingObjectPosition mop = RayTrace.create(player).distance(64D).trace();
				player.addChatComponentMessage(new ChatComponentText(mop.toString()));
				if (mop.typeOfHit == MovingObjectType.BLOCK) {
					queued = true;
					Camera.queue(new AbstractCameraAction() {
						private int frames = 0;

						@Override
						public boolean cameraRender(EntityLivingBase view, float tick) {
							view.setVelocity(0.0D, 0.0D, 0.0D);
							view.lastTickPosX = view.prevPosX = view.posX = mop.hitVec.xCoord;
							view.lastTickPosY = view.prevPosY = view.posY = mop.hitVec.yCoord + 0.5F;
							view.lastTickPosZ = view.prevPosZ = view.posZ = mop.hitVec.zCoord;
							return TOTAL_FRAMES < ++frames;
						}

						@Override
						public void restoreState(EntityLivingBase view, float tick) {
							super.restoreState(view, tick);
							queued = false;
						}
					});
				} else if (mop.typeOfHit == MovingObjectType.ENTITY) {
					queued = true;
					Camera.queue(new AbstractCameraAction() {
						private int frames = 0;

						@Override
						public boolean cameraRender(EntityLivingBase view, float tick) {
							view.lastTickPosX = view.prevPosX = view.posX = mop.entityHit.posX;
							view.lastTickPosY = view.prevPosY = view.posY = mop.entityHit.posY;
							view.lastTickPosZ = view.prevPosZ = view.posZ = mop.entityHit.posZ;
							view.rotationYaw = mop.entityHit.rotationYaw;
							view.prevRotationYaw = mop.entityHit.prevRotationYaw;
							view.rotationPitch = mop.entityHit.rotationPitch;
							view.prevRotationPitch = mop.entityHit.prevRotationPitch;
							return TOTAL_FRAMES < ++frames;
						}

						@Override
						public void restoreState(EntityLivingBase view, float tick) {
							super.restoreState(view, tick);
							queued = false;
						}
					});
				}
			} else {
				player.addChatComponentMessage(new ChatComponentText("Already queued!"));
			}
		}
		return super.onItemRightClick(itemStack, world, player);
	}
}
