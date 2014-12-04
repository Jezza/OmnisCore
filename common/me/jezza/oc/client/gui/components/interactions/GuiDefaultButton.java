package me.jezza.oc.client.gui.components.interactions;

import me.jezza.oc.client.gui.components.GuiTexturedButtonExpandable;
import me.jezza.oc.client.gui.interfaces.ITextAlignment;
import me.jezza.oc.client.gui.lib.Colour;
import me.jezza.oc.client.gui.lib.ResourceHelper;
import me.jezza.oc.client.gui.lib.TextAlignment;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiDefaultButton extends GuiTexturedButtonExpandable<GuiDefaultButton> {
    public static final ResourceLocation BUTTON_TEXTURE = ResourceHelper.getOCTexture("gui/defaultButtons.png");

    public final String text;
    protected int textStartX, textStartY, colour;

    public GuiDefaultButton(int x, int y, int width, int height, String text) {
        super(x, y, 0, 20, width, height, 200, 20, 2, 3, 2, 2);
        setTexture(BUTTON_TEXTURE);
        this.text = text;
        setTextAlignment(TextAlignment.CENTRE);
        setColour(Colour.WHITE);
    }

    public GuiDefaultButton setTextAlignment(ITextAlignment textAlignment) {
        textStartX = textAlignment.translateX(this.width, text);
        textStartY = textAlignment.translateY(this.height, text);
        return this;
    }

    public GuiDefaultButton setColour(Colour colour) {
        this.colour = colour.getInt();
        return this;
    }

    public GuiDefaultButton setColour(int colour) {
        this.colour = colour;
        return this;
    }

    @Override
    public void renderBackground(int mouseX, int mouseY) {
        GL11.glEnable(GL11.GL_BLEND);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        super.renderBackground(mouseX, mouseY);
        Colour.resetToWhite();

        drawText();
    }

    protected void drawText() {
        this.drawString(fontRendererObj, text, x + textStartX, y + textStartY, colour);
    }

    @Override
    public int getPassLevel(int mouseX, int mouseY) {
        return !isVisible() ? 0 : isWithinBounds(mouseX, mouseY) ? 2 : 1;
    }

    @Override
    public int getTextureYShift(int pass) {
        return pass * 20;
    }
}
