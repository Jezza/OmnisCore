package me.jezza.oc.client.gui.components;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * No touchy.
 * Use GuiSimpleTexturedButton instead of this.
 */
@SideOnly(Side.CLIENT)
public abstract class GuiTexturedButton<T extends GuiTexturedButton> extends GuiWidget<T> {

    public int u, v;

    public GuiTexturedButton(int x, int y, int u, int v, int width, int height) {
        super(x, y, width, height);
        this.u = u;
        this.v = v;
    }

    @Override
    public void renderBackground(int mouseX, int mouseY) {
        if (!isVisible())
            return;

        int tempX = u;
        int tempY = v;
        int i = getPassLevel(mouseX, mouseY);

        if (i > 0) {
            tempX += getTextureXShift(i);
            tempY += getTextureYShift(i);
        }
        timedOutClick();

        drawTexturedModalRect(x, y, tempX, tempY, width, height);
    }

    public int getPassLevel(int mouseX, int mouseY) {
        return isClicked() ? 2 : (canClick(mouseX, mouseY) ? 1 : 0);
    }

    public int getU() {
        return u;
    }

    public T addU(int amount) {
        this.u += amount;
        return (T) this;
    }

    public T setU(int u) {
        this.u = u;
        return (T) this;
    }

    public int getV() {
        return v;
    }

    public T addV(int amount) {
        this.v += amount;
        return (T) this;
    }

    public T setV(int v) {
        this.v = v;
        return (T) this;
    }

    public void timedOutClick() {
        if ((System.currentTimeMillis() - timeClicked) > getButtonDelay())
            clicked = false;
    }

    public int getButtonDelay() {
        return 125;
    }

    public int getTextureXShift(int pass) {
        return 0;
    }

    public int getTextureYShift(int pass) {
        return 0;
    }

    @Override
    public void renderForeground(int mouseX, int mouseY) {
    }
}
