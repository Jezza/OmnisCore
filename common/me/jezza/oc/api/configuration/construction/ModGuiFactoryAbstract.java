package me.jezza.oc.api.configuration.construction;

import cpw.mods.fml.client.IModGuiFactory;
import net.minecraft.client.Minecraft;

import java.util.Set;

public abstract class ModGuiFactoryAbstract implements IModGuiFactory {
    @Override
    public void initialize(Minecraft minecraftInstance) {
    }

    @Override
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
        return null;
    }

    @Override
    public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement element) {
        return null;
    }

}
