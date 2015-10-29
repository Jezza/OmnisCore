package me.jezza.oc.common.core.config.entries;

import me.jezza.oc.common.core.config.Config.ConfigInteger;
import me.jezza.oc.common.core.config.ConfigEntry;
import net.minecraftforge.common.config.Configuration;

import java.lang.reflect.Field;

public class CEInteger extends ConfigEntry<ConfigInteger, Integer> {
	public CEInteger(Configuration config) {
		super(config);
	}

	@Override
	public Object loadAnnotation(Configuration config, Field field, String fieldName, ConfigInteger annotation, Integer currentValue, Integer defaultValue) {
		String comment = processComment(annotation.comment());
		return getInt(annotation.category(), useableOr(annotation.name(), fieldName), defaultValue, annotation.minValue(), annotation.maxValue(), comment);
	}

	@Override
	public void saveAnnotation(Configuration config, Field field, String fieldName, ConfigInteger annotation, Integer currentValue, Integer defaultValue) {
		String comment = processComment(annotation.comment());
		getIntProperty(annotation.category(), useableOr(annotation.name(), fieldName), defaultValue, annotation.minValue(), annotation.maxValue(), comment).set(currentValue);
	}
}
