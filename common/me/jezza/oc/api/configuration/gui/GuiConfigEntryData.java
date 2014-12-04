package me.jezza.oc.api.configuration.gui;

import cpw.mods.fml.client.config.GuiConfigEntries.IConfigEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.gui.GuiScreen;

import java.util.ArrayList;
import java.util.List;

public class GuiConfigEntryData extends GuiListExtended {

    private List<IConfigEntry> configEntries;

    public GuiConfigEntryData(GuiScreen parent, Minecraft mc) {
        super(mc, parent.width, parent.height, 30, parent.height - 40, 20);
        configEntries = new ArrayList<>();
    }

    @Override
    public IGuiListEntry getListEntry(int slot) {
        return configEntries.get(slot);
    }

    @Override
    protected int getSize() {
        return configEntries.size();
    }

//    @Override
//    public void initGui() {
//        this.width = owningScreen.width;
//        this.height = owningScreen.height;
//
//        this.maxLabelTextWidth = 0;
//        for (IConfigEntry entry : this.listEntries)
//            if (entry.getLabelWidth() > this.maxLabelTextWidth)
//                this.maxLabelTextWidth = entry.getLabelWidth();
//
//        this.top = owningScreen.titleLine2 != null ? 33 : 23;
//        this.bottom = owningScreen.height - 32;
//        this.left = 0;
//        this.right = width;
//        int viewWidth = this.maxLabelTextWidth + 8 + (width / 2);
//        labelX = (this.width / 2) - (viewWidth / 2);
//        controlX = labelX + maxLabelTextWidth + 8;
//        resetX = (this.width / 2) + (viewWidth / 2) - 45;
//
//        this.maxEntryRightBound = 0;
//        for (IConfigEntry entry : this.listEntries)
//            if (entry.getEntryRightBound() > this.maxEntryRightBound)
//                this.maxEntryRightBound = entry.getEntryRightBound();
//
//        scrollBarX = this.maxEntryRightBound + 5;
//        controlWidth = maxEntryRightBound - controlX - 45;
//    }
}
