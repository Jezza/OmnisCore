package me.jezza.oc.api.configuration.construction;

import me.jezza.oc.api.configuration.gui.GuiModConfigSelector;
import net.minecraft.client.gui.GuiScreen;

public class DefaultModGuiFactory extends ModGuiFactoryAbstract {
    @Override
    public Class<? extends GuiScreen> mainConfigGuiClass() {
        return GuiModConfigSelector.class;
    }
}
