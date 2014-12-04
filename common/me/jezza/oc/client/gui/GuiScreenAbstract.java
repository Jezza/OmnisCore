package me.jezza.oc.client.gui;

import me.jezza.oc.client.gui.components.GuiWidget;
import me.jezza.oc.client.gui.interfaces.IGuiRenderHandler;
import me.jezza.oc.client.gui.lib.Colour;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.util.ArrayList;
import java.util.List;

public abstract class GuiScreenAbstract extends GuiScreen implements IGuiRenderHandler {

    private ArrayList<GuiWidget> buttonList;
    private int id = 0;

    public GuiScreenAbstract() {
        buttonList = new ArrayList<>();
    }

    public ArrayList<GuiWidget> getButtonList() {
        return buttonList;
    }

    @Override
    public void initGui() {
        buttonList.clear();
        id = 0;
    }

    /**
     * Pass through for super.buttonList.
     */
    @SuppressWarnings("unchecked")
    public int addDefaultButton(GuiButton button) {
        super.buttonList.add(button);
        return button.id;
    }

    public int addButton(GuiWidget widget) {
        buttonList.add(widget.setID(getNextID()).setRenderHandler(this));
        return id;
    }

    public int getNextID() {
        return id++;
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int mouseX, int mouseY, float tick) {
        this.drawDefaultBackground();
        drawGuiBackgroundLayer(mouseX, mouseY, tick);

        // Revisit this shit.
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        RenderHelper.disableStandardItemLighting();

        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        super.drawScreen(mouseX, mouseY, tick);

        RenderHelper.enableGUIStandardItemLighting();

        GL11.glDisable(GL11.GL_LIGHTING);
        drawGuiForegroundLayer(mouseX, mouseY);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        RenderHelper.enableStandardItemLighting();
    }

    public void drawGuiBackgroundLayer(int mouseX, int mouseY, float tick) {
        for (GuiWidget widget : buttonList)
            widget.renderBackground(mouseX, mouseY);
    }

    public void drawGuiForegroundLayer(int mouseX, int mouseY) {
        for (GuiWidget widget : buttonList)
            widget.renderForeground(mouseX, mouseY);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int t) {
        for (GuiWidget widget : buttonList)
            if (widget.canClick(mouseX, mouseY)) {
                widget.onClick(mouseX, mouseY, t);
                onActionPerformed(widget, t);
                //break;
            }
    }

    @Override
    public GuiScreen getGuiScreen() {
        return this;
    }

    @Override
    public void renderTooltip(ItemStack itemStack, int x, int y) {
        if (itemStack != null)
            renderTooltip(itemStack, x, y);
    }

    @Override
    public void renderHoveringText(List list, int x, int y, FontRenderer font) {
        drawHoveringText(list, x, y, font);
    }

    protected void drawCentredText(int xOffset, int yOffset, String text) {
        drawCentredText(xOffset, yOffset, text, Colour.WHITE);
    }

    protected void drawCentredText(int xOffset, int yOffset, String text, Colour colour) {
        fontRendererObj.drawString(text, ((width - fontRendererObj.getStringWidth(text)) / 2) + xOffset, (height) / 2 + yOffset, colour.getInt());
    }

    public abstract void onActionPerformed(GuiWidget widget, int mouse);
}
