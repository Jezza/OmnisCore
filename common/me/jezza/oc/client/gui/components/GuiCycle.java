package me.jezza.oc.client.gui.components;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import me.jezza.oc.common.core.utils.MathHelper;

/**
 * If you do instantiate this class, make sure to pass the class to the super.
 */
@SideOnly(Side.CLIENT)
public abstract class GuiCycle<T extends GuiCycle> extends GuiTexturedButton<T> {

    public int typeState;
    private int srcX, srcY;

    public GuiCycle(int x, int y, int u, int v, int width, int height) {
        super(x, y, u, v, width, height);
        srcX = u;
        srcY = v;
    }

    public T setTypeState(int typeState) {
        this.typeState = typeState;
        u = srcX + getShiftedX(typeState);
        v = srcY + getShiftedY(typeState);
        return (T) this;
    }

    @Override
    public boolean onClick(int mouseX, int mouseY, int mouseClick) {
        super.onClick(mouseX, mouseY, mouseClick);
        boolean isClicked = isClicked();
        if (isClicked) {
            processMouseClick(mouseClick);
            typeState = MathHelper.wrapInt(typeState, getNumberOfStates() - 1);
        }

        u = srcX + getShiftedX(typeState);
        v = srcY + getShiftedY(typeState);
        return isClicked;
    }

    /**
     * Will wrap the int afterwards, but not continuously.
     * EG: typeState = 5, but can only exist as a 0 or a 1.
     * typeState = 5, > 1
     * typeState = 0
     * Same in reverse.
     */
    public void processMouseClick(int mouseClick) {
        switch (mouseClick) {
            case 0:
                typeState++;
            case 1:
                typeState--;
            case 2:
                typeState = 0;
        }
    }

    public abstract int getNumberOfStates();

    public int getShiftedX(int typeState) {
        return 0;
    }

    public int getShiftedY(int typeState) {
        return 0;
    }

}
