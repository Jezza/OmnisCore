package me.jezza.oc.client.gui.lib;

import me.jezza.oc.common.core.CoreProperties;
import net.minecraft.util.ResourceLocation;

public class ResourceHelper {

    public static ResourceLocation getMinecraftResource(String texture) {
        return new ResourceLocation(texture);
    }

    public static ResourceLocation getDCResource(String texture) {
        return getModResource(CoreProperties.MOD_ID, texture);
    }

    public static ResourceLocation getDCTexture(String texture) {
        return getDCResource("textures/" + texture);
    }

    public static ResourceLocation getModResource(String modID, String texture) {
        return new ResourceLocation(modID + ":" + texture);
    }

}
