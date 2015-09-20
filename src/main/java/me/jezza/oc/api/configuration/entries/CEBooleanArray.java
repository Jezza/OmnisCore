package me.jezza.oc.api.configuration.entries;

import me.jezza.oc.api.configuration.Config.ConfigBooleanArray;
import me.jezza.oc.api.configuration.ConfigEntry;
import net.minecraftforge.common.config.Configuration;

import java.lang.reflect.Field;

public class CEBooleanArray extends ConfigEntry<ConfigBooleanArray, boolean[]> {
    public CEBooleanArray(Configuration config) {
        super(config);
    }

    @Override
    public Object loadAnnotation(Configuration config, Field field, String fieldName, ConfigBooleanArray annotation, boolean[] currentValue, boolean[] defaultValue) {
        String comment = processComment(annotation.comment());
        return getBooleanArray(annotation.category(), fieldName, defaultValue, comment, annotation.maxListLength());
    }

    @Override
    public void saveAnnotation(Configuration config, Field field, String fieldName, ConfigBooleanArray annotation, boolean[] currentValue, boolean[] defaultValue) {
        String comment = processComment(annotation.comment());
        getBooleanArrayProperty(annotation.category(), fieldName, defaultValue, comment, annotation.maxListLength()).set(currentValue);
    }
}
