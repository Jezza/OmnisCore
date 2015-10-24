package me.jezza.oc.common.core.config.entries;

import me.jezza.oc.api.config.Config.ConfigStringArray;
import me.jezza.oc.common.core.config.ConfigEntry;
import net.minecraftforge.common.config.Configuration;

import java.lang.reflect.Field;

public class CEStringArray extends ConfigEntry<ConfigStringArray, String[]> {
	public CEStringArray(Configuration config) {
		super(config);
	}

	@Override
	public Object loadAnnotation(Configuration config, Field field, String fieldName, ConfigStringArray annotation, String[] currentValue, String[] defaultValue) {
		String comment = processComment(annotation.comment());
		return getStringArray(annotation.category(), fieldName, defaultValue, comment);
	}

	@Override
	public void saveAnnotation(Configuration config, Field field, String fieldName, ConfigStringArray annotation, String[] currentValue, String[] defaultValue) {
		String comment = processComment(annotation.comment());
		getStringArrayProperty(annotation.category(), fieldName, defaultValue, comment).set(currentValue);
	}
}
