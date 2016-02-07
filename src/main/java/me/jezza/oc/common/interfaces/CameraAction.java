package me.jezza.oc.common.interfaces;

import net.minecraft.entity.EntityLivingBase;

/**
 * @author Jezza
 */
public interface CameraAction {
	void storeState(EntityLivingBase view, float tick);

	boolean cameraRender(EntityLivingBase view, float tick);

	void restoreState(EntityLivingBase view, float tick);

	void cleanup(EntityLivingBase view, float tick);
}
