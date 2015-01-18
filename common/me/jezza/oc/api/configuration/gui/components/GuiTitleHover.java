package me.jezza.oc.api.configuration.gui.components;

import com.google.common.collect.Lists;
import me.jezza.oc.client.gui.components.interactions.GuiUntexturedButton;

import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class GuiTitleHover extends GuiUntexturedButton {

    private List<String> hoverText;

    public GuiTitleHover(int x, int y, int width, int height, String... hoverText) {
        super(x, y, width, height);
        this.hoverText = Lists.newArrayList(hoverText);
    }

    @Override
    public void renderForeground(int mouseX, int mouseY, int translatedX, int translatedY) {
        if (canClick(mouseX, mouseY)){
            renderHoveringText(hoverText, translatedX, translatedY, fontRendererObj);
            glDisable(GL_LIGHTING);
        }
    }
}
