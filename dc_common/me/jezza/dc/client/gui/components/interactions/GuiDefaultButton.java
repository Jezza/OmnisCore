package me.jezza.dc.client.gui.components.interactions;

import me.jezza.dc.client.gui.components.GuiTexturedButton;
import me.jezza.dc.client.gui.interfaces.ITextAlignment;
import me.jezza.dc.client.gui.lib.Colour;
import me.jezza.dc.client.gui.lib.ResourceHelper;
import me.jezza.dc.client.gui.lib.TextAlignment;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiDefaultButton extends GuiTexturedButton<GuiDefaultButton> {
    public static final ResourceLocation buttonTexture = ResourceHelper.getMinecraftResource("textures/gui/widgets.png");

    private String text;
    private int textStartX, textStartY, colour;

    /**
     * @param x
     * @param y
     * @param width  Should be less than 399.
     * @param height Should be a fixed number: 20.
     * @param text
     */
    public GuiDefaultButton(int x, int y, int width, int height, String text) {
        super(x, y, 0, 46, width, height);
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

    /**
     * Untested...
     */
    @Override
    public void renderBackground(int mouseX, int mouseY) {
        bindTexture(buttonTexture);
        timedOutClick();
        Colour.resetToWhite();

        int pass = getPassLevel(mouseX, mouseY);
        int textureYShift = getTextureYShift(pass);
        GL11.glEnable(GL11.GL_BLEND);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        int halfWidth = width / 2;
        this.drawTexturedModalRect(x, y, 0, v + textureYShift, halfWidth, height);
        this.drawTexturedModalRect(x + halfWidth, y, (200 - halfWidth), v + textureYShift, halfWidth, height);

        this.drawString(fontRendererObj, text, x + textStartX, y + textStartY, colour);
    }

    @Override
    public int getPassLevel(int mouseX, int mouseY) {
        return !isVisible() ? 0 : (canClick(mouseX, mouseY) ? 2 : 1);
    }

    @Override
    public int getTextureYShift(int pass) {
        return pass * 20;
    }
}
