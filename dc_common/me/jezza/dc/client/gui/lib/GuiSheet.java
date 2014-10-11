package me.jezza.dc.client.gui.lib;

import me.jezza.dc.common.core.CoreProperties;
import net.minecraft.util.ResourceLocation;

public class GuiSheet {
    private static final String GUI_SHEET_LOCATION = "textures/gui/";

    public static final ResourceLocation GUI_DEFAULT_BUTTON = getResource("defaultButton");

    private static ResourceLocation getResource(String loc) {
        return new ResourceLocation(CoreProperties.MOD_ID, GUI_SHEET_LOCATION + loc + ".png");
    }
}
