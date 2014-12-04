package me.jezza.oc.client.gui.components.interactions;

import me.jezza.oc.client.gui.interfaces.ITextAlignment;

public class GuiUnicodeGlyphButton extends GuiDefaultButton {

    private final String glyph;
    private float scale = 1.0F;

    public GuiUnicodeGlyphButton(int x, int y, int width, int height, String text, String glyph) {
        super(x, y, width, height, text);
        this.glyph = glyph;
    }

    public GuiUnicodeGlyphButton setGlyphScale(float scale) {
        this.scale = scale;
        return this;
    }

    @Override
    public GuiDefaultButton setTextAlignment(ITextAlignment textAlignment) {
        String mainText = glyph + " " + text;
        textStartX = textAlignment.translateX(this.width, mainText);
        textStartY = textAlignment.translateY(this.height, mainText);
        return this;
    }

    @Override
    protected void drawText() {


        drawString(fontRendererObj, text, x + textStartX + 5, y + textStartY, colour);
    }

    @Override
    public int getTextureYShift(int pass) {
        return pass * 20;
    }

}
