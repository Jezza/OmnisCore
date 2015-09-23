package me.jezza.oc.common.core.config.entries;

import me.jezza.oc.api.config.Config.ConfigBoolean;
import me.jezza.oc.common.core.config.ConfigEntry;
import net.minecraftforge.common.config.Configuration;

import java.lang.reflect.Field;

public class CEBoolean extends ConfigEntry<ConfigBoolean, Boolean> {
    public CEBoolean(Configuration config) {
        super(config);
    }

    @Override
    public Object loadAnnotation(Configuration config, Field field, String fieldName, ConfigBoolean annotation, Boolean currentValue, Boolean defaultValue) {
        return getBoolean(annotation.category(), fieldName, defaultValue, processComment(annotation.comment()));
    }

    @Override
    public void saveAnnotation(Configuration config, Field field, String fieldName, ConfigBoolean annotation, Boolean currentValue, Boolean defaultValue) {
        getBooleanProperty(annotation.category(), fieldName, defaultValue, processComment(annotation.comment())).set(currentValue);
    }
}
