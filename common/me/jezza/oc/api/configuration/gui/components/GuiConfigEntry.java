package me.jezza.oc.api.configuration.gui.components;

import me.jezza.oc.client.gui.interfaces.IGuiSlotEntry;
import net.minecraft.client.renderer.Tessellator;

public class GuiConfigEntry implements IGuiSlotEntry{
    @Override
    public int getWidth() {
        return 0;
    }

    @Override
    public void drawEntry(int p_148279_1_, int p_148279_2_, int p_148279_3_, int p_148279_4_, int p_148279_5_, Tessellator p_148279_6_, int p_148279_7_, int p_148279_8_, boolean p_148279_9_) {
    }

    @Override
    public boolean mousePressed(int p_148278_1_, int p_148278_2_, int p_148278_3_, int p_148278_4_, int p_148278_5_, int p_148278_6_) {
        return false;
    }

    @Override
    public void mouseReleased(int p_148277_1_, int p_148277_2_, int p_148277_3_, int p_148277_4_, int p_148277_5_, int p_148277_6_) {

    }
}
