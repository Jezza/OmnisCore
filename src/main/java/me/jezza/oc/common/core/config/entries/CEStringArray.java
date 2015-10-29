package me.jezza.oc.common.core.config.entries;

import me.jezza.oc.common.core.config.Config.ConfigStringArray;
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
		return getStringArray(annotation.category(), useableOr(annotation.name(), fieldName), defaultValue, comment);
	}

	@Override
	public void saveAnnotation(Configuration config, Field field, String fieldName, ConfigStringArray annotation, String[] currentValue, String[] defaultValue) {
		String comment = processComment(annotation.comment());
		getStringArrayProperty(annotation.category(), useableOr(annotation.name(), fieldName), defaultValue, comment).set(currentValue);
	}
}
