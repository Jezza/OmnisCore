package me.jezza.oc.api.configuration.gui;

import cpw.mods.fml.client.GuiModList;
import cpw.mods.fml.common.ModContainer;
import me.jezza.oc.api.configuration.ConfigHandler;
import me.jezza.oc.api.configuration.discovery.ConfigContainer;
import me.jezza.oc.api.configuration.discovery.ConfigData;
import me.jezza.oc.client.gui.GuiScreenAbstract;
import me.jezza.oc.client.gui.components.GuiWidget;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class GuiModConfigSelector extends GuiScreenAbstract {

    private final GuiScreen parentScreen;

    private ModContainer modContainer;
    private boolean requiresSelection;
    private ConfigData configData;
    private List<ConfigContainer> containerList;

    private String modName;
    private String title;
    private String subTitle;

    public GuiModConfigSelector(GuiScreen parentScreen) throws NullPointerException, NoSuchFieldException, IllegalAccessException {
        this.parentScreen = parentScreen;

        GuiModList modList = (GuiModList) parentScreen;

        Field field = modList.getClass().getDeclaredField("selectedMod");
        field.setAccessible(true);
        ModContainer modContainer = (ModContainer) field.get(parentScreen);
        if (modContainer == null)
            throw new NullPointerException("GuiModList.modContainer");
        this.modContainer = modContainer;

        configData = ConfigHandler.getConfigData(modContainer);
        requiresSelection = configData.requiresSelection();

        // No more processing is needed. The object will be returned normally.
        if (!requiresSelection)
            return;

        containerList = new ArrayList<>();
        configData.populateList(containerList);

        modName = modContainer.getName();
        title = I18n.format("oc.config.gui.selector.title.1");
        subTitle = I18n.format("oc.config.gui.selector.title.2");
    }

    @Override
    public void initGui() {
        if (!requiresSelection)
            mc.displayGuiScreen(new GuiModConfigDefault(parentScreen, modContainer, configData.getFirstContainer()));

        super.initGui();

    }

    @Override
    public void onActionPerformed(GuiWidget widget, int mouse) {
    }
}
