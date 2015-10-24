package me.jezza.oc.common.core.config.entries;

import me.jezza.oc.api.config.Config.ConfigFloat;
import me.jezza.oc.common.core.config.ConfigEntry;
import net.minecraftforge.common.config.Configuration;

import java.lang.reflect.Field;

public class CEFloat extends ConfigEntry<ConfigFloat, Float> {
	public CEFloat(Configuration config) {
		super(config);
	}

	@Override
	public Object loadAnnotation(Configuration config, Field field, String fieldName, ConfigFloat annotation, Float currentValue, Float defaultValue) {
		String comment = processComment(annotation.comment());
		return getFloat(annotation.category(), fieldName, defaultValue, annotation.minValue(), annotation.maxValue(), comment);
	}

	@Override
	public void saveAnnotation(Configuration config, Field field, String fieldName, ConfigFloat annotation, Float currentValue, Float defaultValue) {
		String comment = processComment(annotation.comment());
		getFloatProperty(annotation.category(), fieldName, defaultValue, annotation.minValue(), annotation.maxValue(), comment).set(currentValue);
	}
}
