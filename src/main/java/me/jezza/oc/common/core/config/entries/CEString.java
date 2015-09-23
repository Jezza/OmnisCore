package me.jezza.oc.common.core.config.entries;

import me.jezza.oc.api.config.Config.ConfigString;
import me.jezza.oc.common.core.config.ConfigEntry;
import net.minecraftforge.common.config.Configuration;

import java.lang.reflect.Field;

public class CEString extends ConfigEntry<ConfigString, String> {
    public CEString(Configuration config) {
        super(config);
    }

    @Override
    public Object loadAnnotation(Configuration config, Field field, String fieldName, ConfigString annotation, String currentValue, String defaultValue) {
        String comment = processComment(annotation.comment());
        return getString(annotation.category(), fieldName, defaultValue, comment, annotation.validValues());
    }

    @Override
    public void saveAnnotation(Configuration config, Field field, String fieldName, ConfigString annotation, String currentValue, String defaultValue) {
        String comment = processComment(annotation.comment());
        getStringProperty(annotation.category(), fieldName, defaultValue, comment, annotation.validValues()).set(currentValue);
    }
}
