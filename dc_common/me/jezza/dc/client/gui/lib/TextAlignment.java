package me.jezza.dc.client.gui.lib;

public enum TextAlignment {
    TOP_LEFT(-1, -1),
    TOP_CENTRE(0, -1),
    TOP_RIGHT(1, -1),
    LEFT(-1, 0),
    CENTRE(0, 0),
    RIGHT(1, 0),
    BOTTOM_LEFT(-1, 1),
    BOTTOM_CENTRE(0, 1),
    BOTTOM_RIGHT(1, 1);

    private int xOffset, yOffset;

    TextAlignment(int xOffset, int yOffset) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }

    public int getXOffset() {
        return xOffset;
    }

    public int getYOffset() {
        return yOffset;
    }
}
