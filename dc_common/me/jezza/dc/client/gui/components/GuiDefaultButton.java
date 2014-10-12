package me.jezza.dc.client.gui.components;

import me.jezza.dc.client.gui.lib.Colour;
import me.jezza.dc.client.gui.lib.GuiSheet;
import me.jezza.dc.client.gui.lib.TextAlignment;
import net.minecraft.util.ResourceLocation;

public class GuiDefaultButton extends GuiTexturedButton {

    public static final ResourceLocation buttonTextures = new ResourceLocation("textures/gui/widgets.png");

    private String text;
    private TextAlignment textAlignment;
    private Colour colour;

    public GuiDefaultButton(int x, int y, String text) {
        super(x, y, 0, 0, 16, 16);
        this.text = text;
        textAlignment = TextAlignment.CENTRE;
        colour = Colour.WHITE;
    }

    public GuiDefaultButton setTextAlignment(TextAlignment textAlignment) {
        this.textAlignment = textAlignment;
        return this;
    }

    public GuiDefaultButton setColour(Colour colour) {
        this.colour = colour;
        return this;
    }

    @Override
    public void render(int mouseX, int mouseY) {
        bindTexture(GuiSheet.GUI_DEFAULT_BUTTON);
        super.render(mouseX, mouseY);
        int xTextStart = 0;
        int yTextStart = 0;
//        fontRendererObj.drawString(text, x, y, colour.getInt());
    }

    @Override
    public int getTextureXShift(int pass) {
        return pass * 16;
    }
}
