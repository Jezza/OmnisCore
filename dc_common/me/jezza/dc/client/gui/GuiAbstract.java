package me.jezza.dc.client.gui;

import me.jezza.dc.client.gui.components.GuiWidget;
import me.jezza.dc.client.gui.interfaces.IGuiRenderHandler;
import me.jezza.dc.client.gui.lib.Colour;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public abstract class GuiAbstract extends GuiContainer implements IGuiRenderHandler {
    public int middleX, middleY;
    public ResourceLocation mainTexture;

    private ArrayList<GuiWidget> buttonList;
    private int id = 0;

    protected GuiAbstract() {
        this(new Container() {
            @Override
            public boolean canInteractWith(EntityPlayer var1) {
                return true;
            }
        });
    }

    public GuiAbstract(Container container) {
        super(container);
        buttonList = new ArrayList<GuiWidget>();
    }

    public GuiAbstract setMainTexture(ResourceLocation mainTexture) {
        this.mainTexture = mainTexture;
        return this;
    }

    public ArrayList<GuiWidget> getButtonList() {
        return buttonList;
    }

    public void bindTexture() {
        bindTexture(mainTexture);
    }

    public void bindTexture(ResourceLocation texture) {
        mc.renderEngine.bindTexture(texture);
    }

    @Override
    public void initGui() {
        super.initGui();
        buttonList.clear();
        id = 0;

        middleX = (width - xSize) / 2;
        middleY = (height - ySize) / 2;
    }

    public int getNextID() {
        return id++;
    }

    /**
     * Pass through for super buttonlist.
     */
    public int addDefaultButton(GuiButton button) {
        super.buttonList.add(button);
        return id;
    }

    public int addButton(GuiWidget widget) {
        buttonList.add(widget.setID(getNextID()).setRenderHandler(this));
        return id;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float var1, int mouseX, int mouseY) {
        if (mainTexture != null)
            mc.renderEngine.bindTexture(mainTexture);

        for (GuiWidget widget : buttonList)
            widget.render(mouseX, mouseY);
        for (GuiWidget widget : buttonList)
            widget.postRender(mouseX, mouseY);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int t) {
        for (GuiWidget widget : buttonList)
            if (widget.canClick(mouseX, mouseY)) {
                widget.onClick(mouseX, mouseY, t);
                onActionPerformed(widget, t);
            }
    }

    @Override
    public GuiScreen getGuiScreen() {
        return this;
    }

    @Override
    public void renderTooltip(ItemStack itemStack, int x, int y) {
        if (itemStack != null)
            renderToolTip(itemStack, x, y);
    }

    @Override
    public void renderHoveringText(List list, int x, int y, FontRenderer font) {
        drawHoveringText(list, x, y, font);
    }

    protected void drawCentredText(int xOffset, int yOffset, String text) {
        drawCentredText(xOffset, yOffset, text, Colour.WHITE);
    }

    protected void drawCentredText(int xOffset, int yOffset, String text, Colour colour) {
        fontRendererObj.drawString(text, ((xSize - fontRendererObj.getStringWidth(text)) / 2) + xOffset, (ySize) / 2 + yOffset, colour.getInt());
    }

    public abstract void onActionPerformed(GuiWidget widget, int mouse);

}