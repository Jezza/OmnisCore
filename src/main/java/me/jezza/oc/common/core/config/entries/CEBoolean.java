package me.jezza.oc.common.core.config.entries;

import me.jezza.oc.common.core.config.Config.ConfigBoolean;
import me.jezza.oc.common.core.config.ConfigEntry;
import me.jezza.oc.common.core.config.OmnisConfiguration;
import me.jezza.oc.common.utils.Classes;

import java.lang.reflect.Field;

import static me.jezza.oc.common.utils.helpers.StringHelper.useable;

public class CEBoolean extends ConfigEntry<ConfigBoolean, Boolean> {
	public CEBoolean(OmnisConfiguration config) {
		super(config);
	}

	@Override
	protected boolean checkField(Field field) {
		return Classes.unwrap(field.getType()) == boolean.class;
	}

	@Override
	protected String fieldName(Field field, ConfigBoolean annotation) {
		String name = annotation.name();
		return useable(name) ? name : super.fieldName(field, annotation);
	}

	@Override
	public Object load(OmnisConfiguration config, Field field, String name, ConfigBoolean annotation, Boolean currentValue, Boolean defaultValue) {
		return config.getBoolean(annotation.category(), name, defaultValue, processComment(annotation.comment()));
	}

	@Override
	public void save(OmnisConfiguration config, Field field, String name, ConfigBoolean annotation, Boolean currentValue, Boolean defaultValue) {
		config.getBooleanProperty(annotation.category(), name, defaultValue, processComment(annotation.comment())).set(currentValue);
	}
}
