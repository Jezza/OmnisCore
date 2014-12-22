package me.jezza.oc.api.configuration.gui;

import cpw.mods.fml.client.GuiModList;
import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.IConfigElement;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.relauncher.ReflectionHelper;
import me.jezza.oc.api.configuration.ConfigHandler;
import me.jezza.oc.api.configuration.discovery.ConfigContainer;
import me.jezza.oc.api.configuration.discovery.ConfigData;
import me.jezza.oc.client.gui.GuiScreenAbstract;
import me.jezza.oc.client.gui.components.GuiWidget;
import me.jezza.oc.common.core.CoreProperties;
import me.jezza.oc.common.utils.Localise;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is basically one giant hack.
 * I feel sorry for all eyes who look upon this code.
 * How do you think I feel? I wrote this shit... :/
 * <p/>
 * Just a quick run down why I did what I did.
 * Firstly, you may notice a return statement in the constructor.
 * If you're learning Java, never do this.
 * Elephants will crap on your house if you do.
 * This is a bad thing to do.
 * The reason I return is so I can safely set the Gui screen if only one configContainer has been found for the mod.
 * That means only one controller has been located and loaded with the mod in question.
 * <p/>
 * The reason I can't set it in the constructor, is because you've got to remember that this class is being constructed to set as the currentScreen.
 * This class is constructed by GuiModList upon clicking config.
 * Hence the parameter.
 * I need to grab what mod was selected to actually check if they have any containers, let alone multiple of them.
 * If they only have one, I can stop processing and let the object be created.
 * Once it returns, it is viewed as constructed, so from there, the main code inside GuiModList finally sets that instance as the main screen.
 * <p/>
 * One of the first methods that is called, among others, is initGui();
 * This is so you can create all needed data that require something only instantiated after the main constructor.
 * It's called after resizing, etc.
 * But if requiresSelection is true, I can sneakily swap out the screens for the only option for the configContainer.
 * This is before anything is even rendered, so you see no flicker, or change of screen.
 * <p/>
 * Otherwise, I continue, so you can select which controller to configure.
 */
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
        ModContainer modContainer = ReflectionHelper.getPrivateValue(GuiModList.class, modList, "selectedMod");
        if (modContainer == null)
            throw new NullPointerException("GuiModList.modContainer");
        this.modContainer = modContainer;

        configData = ConfigHandler.getConfigData(modContainer);

        if (configData == null)
            throw new IllegalStateException(modContainer.getName() + " is calling OmnisCore default Gui without having any Config.Controllers. Please correct this.");

        requiresSelection = configData.requiresSelection();

        CoreProperties.logger.fatal(requiresSelection ? "Mod requires selection" : "Mod doesn't require");

        // No more processing is needed. The object will be returned normally.
        if (!requiresSelection)
            return;

        containerList = new ArrayList<>();
        configData.populateList(containerList);

        modName = modContainer.getName();
        title = Localise.format("oc.config.gui.selector.title.1");
        subTitle = Localise.format("oc.config.gui.selector.title.2");
    }

    @Override
    public void initGui() {
        if (!requiresSelection) {
            displayGuiWithContainer(configData.getFirstContainer());
            return;
        }

        super.initGui();
    }

    private void displayGuiWithContainer(ConfigContainer configContainer) {
        Configuration config = configContainer.getConfig();
        List<IConfigElement> configElements = new ArrayList<>();
        for (String categoryName : config.getCategoryNames())
            configElements.addAll(new ConfigElement(config.getCategory(categoryName)).getChildElements());
        mc.displayGuiScreen(new GuiConfig(parentScreen, configElements, modContainer.getModId(), false, false, modContainer.getName(), configContainer.getConfigPath()));
    }

    @Override
    public void onActionPerformed(GuiWidget widget, int mouse) {
    }
}
