package me.jezza.dc.client.gui.components;

public abstract class GuiToggle extends GuiCycle {

    public GuiToggle(int x, int y, int u, int v, int width, int height) {
        super(x, y, u, v, width, height);
    }

    @Override
    public int getNumberOfStates() {
        return 2;
    }

}
