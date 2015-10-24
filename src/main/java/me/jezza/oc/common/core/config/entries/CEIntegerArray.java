package me.jezza.oc.common.core.config.entries;

import me.jezza.oc.api.config.Config.ConfigIntegerArray;
import me.jezza.oc.common.core.config.ConfigEntry;
import net.minecraftforge.common.config.Configuration;

import java.lang.reflect.Field;

public class CEIntegerArray extends ConfigEntry<ConfigIntegerArray, int[]> {
	public CEIntegerArray(Configuration config) {
		super(config);
	}

	@Override
	public Object loadAnnotation(Configuration config, Field field, String fieldName, ConfigIntegerArray annotation, int[] currentValue, int[] defaultValue) {
		String comment = processComment(annotation.comment());
		return getIntArray(annotation.category(), fieldName, defaultValue, comment, annotation.minValue(), annotation.maxValue(), annotation.maxListLength());
	}

	@Override
	public void saveAnnotation(Configuration config, Field field, String fieldName, ConfigIntegerArray annotation, int[] currentValue, int[] defaultValue) {
		String comment = processComment(annotation.comment());
		getIntArrayProperty(annotation.category(), fieldName, defaultValue, comment, annotation.minValue(), annotation.maxValue(), annotation.maxListLength()).set(currentValue);
	}
}
