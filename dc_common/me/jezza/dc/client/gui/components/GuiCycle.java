package me.jezza.dc.client.gui.components;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import me.jezza.dc.common.core.MathHelper;

@SideOnly(Side.CLIENT)
public abstract class GuiCycle extends GuiTexturedButton {

    public int typeState;

    private int srcX, srcY;

    public GuiCycle(int x, int y, int u, int v, int width, int height) {
        super(x, y, u, v, width, height);
        srcX = u;
        srcY = v;
    }

    public GuiCycle setTypeState(int typeState) {
        this.typeState = typeState;
        u = srcX + getShiftedX(typeState);
        v = srcY + getShiftedY(typeState);
        return this;
    }

    @Override
    public boolean onClick(int mouseX, int mouseY, int mouseClick) {
        super.onClick(mouseX, mouseY, mouseClick);
        if (isClicked()) {
            if (mouseClick == 0)
                typeState++;
            if (mouseClick == 1)
                typeState--;
            if (mouseClick == 2)
                typeState = 0;
            typeState = MathHelper.wrapInt(typeState, getNumberOfStates() - 1);
        }

        u = srcX + getShiftedX(typeState);
        v = srcY + getShiftedY(typeState);

        return isClicked();
    }

    public abstract int getNumberOfStates();

    public int getShiftedX(int typeState) {
        return 0;
    }

    public int getShiftedY(int typeState) {
        return 0;
    }

}
