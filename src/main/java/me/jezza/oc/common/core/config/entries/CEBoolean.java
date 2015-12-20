package me.jezza.oc.common.core.config.entries;

import me.jezza.oc.common.core.config.Config.ConfigBoolean;
import me.jezza.oc.common.core.config.ConfigEntry;
import me.jezza.oc.common.core.config.OmnisConfiguration;
import me.jezza.oc.common.interfaces.InputBuffer;
import me.jezza.oc.common.interfaces.OutputBuffer;
import me.jezza.oc.common.utils.Classes;
import me.jezza.oc.common.utils.helpers.StringHelper;

import java.lang.reflect.Field;

public class CEBoolean extends ConfigEntry<ConfigBoolean, Boolean> {
	public CEBoolean(OmnisConfiguration config) {
		super(config);
	}

	@Override
	protected boolean checkField(Field field) {
		return Classes.unwrap(field.getType()) == boolean.class;
	}

	@Override
	public Object load(OmnisConfiguration config, Field field, String fieldName, ConfigBoolean annotation, Boolean currentValue, Boolean defaultValue) {
		return config.getBoolean(annotation.category(), StringHelper.firstUseable(annotation.name(), fieldName), defaultValue, processComment(annotation.comment()));
	}

	@Override
	public void save(OmnisConfiguration config, Field field, String fieldName, ConfigBoolean annotation, Boolean currentValue, Boolean defaultValue) {
		config.getBooleanProperty(annotation.category(), StringHelper.firstUseable(annotation.name(), fieldName), defaultValue, processComment(annotation.comment())).set(currentValue);
	}
}
