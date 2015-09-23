package me.jezza.oc.common.core.config.entries;

import me.jezza.oc.api.config.Config.ConfigDoubleArray;
import me.jezza.oc.common.core.config.ConfigEntry;
import net.minecraftforge.common.config.Configuration;

import java.lang.reflect.Field;

public class CEDoubleArray extends ConfigEntry<ConfigDoubleArray, double[]> {
    public CEDoubleArray(Configuration config) {
        super(config);
    }

    @Override
    public Object loadAnnotation(Configuration config, Field field, String fieldName, ConfigDoubleArray annotation, double[] currentValue, double[] defaultValue) {
        String comment = processComment(annotation.comment());
        return getDoubleArray(annotation.category(), fieldName, defaultValue, comment, annotation.minValue(), annotation.maxValue(), annotation.maxListLength());
    }

    @Override
    public void saveAnnotation(Configuration config, Field field, String fieldName, ConfigDoubleArray annotation, double[] currentValue, double[] defaultValue) {
        String comment = processComment(annotation.comment());
        getDoubleArrayProperty(annotation.category(), fieldName, defaultValue, comment, annotation.minValue(), annotation.maxValue(),annotation.maxListLength()).set(currentValue);
    }
}
