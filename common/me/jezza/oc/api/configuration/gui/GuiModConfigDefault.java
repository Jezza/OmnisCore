package me.jezza.oc.api.configuration.gui;

import cpw.mods.fml.client.config.GuiButtonExt;
import cpw.mods.fml.client.config.GuiCheckBox;
import cpw.mods.fml.client.config.GuiConfigEntries.IConfigEntry;
import cpw.mods.fml.client.config.IConfigElement;
import cpw.mods.fml.common.ModContainer;
import me.jezza.oc.api.configuration.discovery.ConfigContainer;
import me.jezza.oc.client.gui.GuiScreenAbstract;
import me.jezza.oc.client.gui.components.GuiWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import org.lwjgl.input.Keyboard;

import java.util.List;

import static cpw.mods.fml.client.config.GuiUtils.RESET_CHAR;
import static cpw.mods.fml.client.config.GuiUtils.UNDO_CHAR;

public class GuiModConfigDefault extends GuiScreenAbstract {

    // Mod specific stuff
    protected ModContainer modContainer;
    protected ConfigContainer configContainer;

    public GuiScreen parentScreen;

    public List<IConfigElement> configElements;
    public List<IConfigEntry> initEntries;

    public String title = "Config GUI";
    public String titleLine2 = "";

    public GuiConfigEntryData entryList;

    protected GuiButtonExt btnDefaultAll;
    protected GuiButtonExt btnUndoAll;
    protected GuiCheckBox chkApplyGlobally;


    public String modID;

    /**
     * When set to a non-null value the OnConfigChanged and PostConfigChanged events will be posted when the Done button is pressed
     * if any configElements were changed (includes child screens). If not defined, the events will be posted if the parent gui is null
     * or if the parent gui is not an instance of GuiConfig.
     */
    public String configID;
    public boolean isWorldRunning;
    public boolean allRequireWorldRestart;
    public boolean allRequireMcRestart;

    public boolean needsRefresh = true;

    public String doneString;
    public String undoString;
    public String resetString;
    public String applyString;

    public GuiModConfigDefault(GuiScreen parentScreen, ModContainer modContainer, ConfigContainer configContainer) {
        this.parentScreen = parentScreen;
        this.modContainer = modContainer;
        this.configContainer = configContainer;
    }

    private GuiModConfigDefault(List<IConfigElement> configElements, String modID, String configID, boolean allRequireWorldRestart, boolean allRequireMcRestart, String title, String titleLine2) {
        this.mc = Minecraft.getMinecraft();
        this.configElements = configElements;

        this.entryList = new GuiConfigEntryData(this, mc);
//        this.initEntries = new ArrayList<>(entryList.listEntries);

        this.modID = modID;
        this.configID = configID;
        this.isWorldRunning = mc.theWorld != null;
        this.allRequireWorldRestart = allRequireWorldRestart;
        this.allRequireMcRestart = allRequireMcRestart;

        if (title != null)
            this.title = title;
        this.titleLine2 = titleLine2;
        if (this.titleLine2 != null && this.titleLine2.startsWith(" > "))
            this.titleLine2 = this.titleLine2.replaceFirst(" > ", "");

        doneString = I18n.format("gui.done");
        undoString = I18n.format("fml.configgui.tooltip.undoChanges");
        resetString = I18n.format("fml.configgui.tooltip.resetToDefault");
        applyString = I18n.format("fml.configgui.applyGlobally");
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    @SuppressWarnings("unchecked")
    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);

        if (entryList == null || this.needsRefresh) {
//            entryList = new GuiConfigEntries(this, mc);
            needsRefresh = false;
        }

        int undoGlyphWidth = fontRendererObj.getStringWidth(UNDO_CHAR) * 2;
        int resetGlyphWidth = fontRendererObj.getStringWidth(RESET_CHAR) * 2;
        int doneWidth = Math.max(fontRendererObj.getStringWidth(doneString) + 20, 100);
        int undoWidth = fontRendererObj.getStringWidth(" " + undoString) + undoGlyphWidth + 20;
        int resetWidth = fontRendererObj.getStringWidth(" " + resetString) + resetGlyphWidth + 20;
        int checkWidth = fontRendererObj.getStringWidth(applyString) + 13;
        int buttonWidthHalf = (doneWidth + 5 + undoWidth + 5 + resetWidth + 5 + checkWidth) / 2;

//        addButton(new me.jezza.oc.client.gui.components.interactions.GuiUnicodeGlyphButton())

//        this.buttonList.add(new GuiButtonExt(2000, this.width / 2 - buttonWidthHalf, this.height - 29, doneWidth, 20, I18n.format("gui.done")));
//        this.buttonList.add(btnDefaultAll = new GuiUnicodeGlyphButton(this.width / 2 - buttonWidthHalf + doneWidth + 5 + undoWidth + 5, this.height - 29, resetWidth, 20, " " + I18n.format("fml.configgui.tooltip.resetToDefault"), RESET_CHAR, 2.0F));
//        this.buttonList.add(btnUndoAll = new GuiUnicodeGlyphButton(this.width / 2 - buttonWidthHalf + doneWidth + 5, this.height - 29, undoWidth, 20, " " + I18n.format("fml.configgui.tooltip.undoChanges"), UNDO_CHAR, 2.0F));
//        this.buttonList.add(chkApplyGlobally = new GuiCheckBox(2003, this.width / 2 - buttonWidthHalf + doneWidth + 5 + undoWidth + 5 + resetWidth + 5, this.height - 24, I18n.format("fml.configgui.applyGlobally"), false));

//        entryList.initGui();
    }

    @Override
    public void onActionPerformed(GuiWidget widget, int mouse) {
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
//        this.entryList.drawScreen(mouseX, mouseY, partialTicks);
        this.drawCenteredString(this.fontRendererObj, this.title, this.width / 2, 8, 16777215);
        String title2 = this.titleLine2;

        if (title2 != null) {
            int strWidth = mc.fontRenderer.getStringWidth(title2);
            int elipsisWidth = mc.fontRenderer.getStringWidth("...");
            if (strWidth > width - 6 && strWidth > elipsisWidth)
                title2 = mc.fontRenderer.trimStringToWidth(title2, width - 6 - elipsisWidth).trim() + "...";
            this.drawCenteredString(this.fontRendererObj, title2, this.width / 2, 18, 16777215);
        }

//        this.btnUndoAll.enabled = this.entryList.areAnyEntriesEnabled(this.chkApplyGlobally.isChecked()) && this.entryList.hasChangedEntry(this.chkApplyGlobally.isChecked());
//        this.btnDefaultAll.enabled = this.entryList.areAnyEntriesEnabled(this.chkApplyGlobally.isChecked()) && !this.entryList.areAllEntriesDefault(this.chkApplyGlobally.isChecked());
//        this.entryList.drawScreenPost(mouseX, mouseY, partialTicks);
//        if (this.undoHoverChecker.checkHover(mouseX, mouseY))
//            this.drawToolTip(this.mc.fontRenderer.listFormattedStringToWidth(I18n.format("fml.configgui.tooltip.undoAll"), 300), mouseX, mouseY);
//        if (this.resetHoverChecker.checkHover(mouseX, mouseY))
//            this.drawToolTip(this.mc.fontRenderer.listFormattedStringToWidth(I18n.format("fml.configgui.tooltip.resetAll"), 300), mouseX, mouseY);
//        if (this.checkBoxHoverChecker.checkHover(mouseX, mouseY))
//            this.drawToolTip(this.mc.fontRenderer.listFormattedStringToWidth(I18n.format("fml.configgui.tooltip.applyGlobally"), 300), mouseX, mouseY);
    }
}
