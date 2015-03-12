package me.jezza.oc.api.configuration.entries;

import me.jezza.oc.api.configuration.Config.ConfigBoolean;
import me.jezza.oc.api.configuration.ConfigEntry;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class ConfigEntryBoolean extends ConfigEntry<ConfigBoolean, Boolean> {

    @Override
    public Object loadAnnotation(Configuration config, String fieldName, ConfigBoolean annotation, Boolean currentValue, Boolean defaultValue) {
        return config.getBoolean(fieldName, annotation.category(), defaultValue, processComment(annotation.comment()));
    }

    @Override
    public void saveAnnotation(Configuration config, String fieldName, ConfigBoolean annotation, Boolean currentValue, Boolean defaultValue) {
        Property prop = config.get(annotation.category(), fieldName, defaultValue, processComment(annotation.comment()));
        prop.setLanguageKey(fieldName);
        prop.comment += " [default: " + defaultValue + "]";
        prop.set(currentValue);
    }
}
