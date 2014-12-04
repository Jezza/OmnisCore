package me.jezza.oc.api.configuration.gui.components;

import me.jezza.oc.client.gui.components.interactions.GuiDefaultButton;
import me.jezza.oc.client.gui.components.interactions.GuiToggle;

public class GuiCheckBox extends GuiToggle {
    private String displayText;

    public GuiCheckBox(int x, int y) {
        super(x, y, 0, 60, 10, 10);
        setTexture(GuiDefaultButton.BUTTON_TEXTURE);
    }

    public GuiCheckBox(int x, int y, String text) {
        super(x, y, 0, 60, 10, 10);
        this.displayText = text;
        width += fontRendererObj.getStringWidth(text);
    }

    @Override
    public int getShiftedX(int typeState) {
        return typeState * 10;
    }
}
