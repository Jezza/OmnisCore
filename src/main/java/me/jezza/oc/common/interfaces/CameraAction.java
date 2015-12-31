package me.jezza.oc.common.interfaces;

import cpw.mods.fml.common.gameevent.TickEvent.RenderTickEvent;
import net.minecraft.entity.EntityLivingBase;

/**
 * @author Jezza
 */
public interface CameraAction {
	boolean cameraRender(RenderTickEvent event, EntityLivingBase view);
}
