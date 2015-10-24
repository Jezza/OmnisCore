package me.jezza.oc.common.core.config.entries;

import me.jezza.oc.api.config.Config.ConfigDouble;
import me.jezza.oc.common.core.config.ConfigEntry;
import net.minecraftforge.common.config.Configuration;

import java.lang.reflect.Field;

public class CEDouble extends ConfigEntry<ConfigDouble, Double> {
	public CEDouble(Configuration config) {
		super(config);
	}

	@Override
	public Object loadAnnotation(Configuration config, Field field, String fieldName, ConfigDouble annotation, Double currentValue, Double defaultValue) {
		String comment = processComment(annotation.comment());
		return getDouble(annotation.category(), fieldName, defaultValue, comment, annotation.minValue(), annotation.maxValue());
	}

	@Override
	public void saveAnnotation(Configuration config, Field field, String fieldName, ConfigDouble annotation, Double currentValue, Double defaultValue) {
		String comment = processComment(annotation.comment());
		getDoubleProperty(annotation.category(), fieldName, defaultValue, comment, annotation.minValue(), annotation.maxValue()).set(currentValue);
	}
}
