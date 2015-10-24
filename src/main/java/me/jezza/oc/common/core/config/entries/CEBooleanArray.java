package me.jezza.oc.common.core.config.entries;

import me.jezza.oc.api.config.Config.ConfigBooleanArray;
import me.jezza.oc.common.core.config.ConfigEntry;
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
