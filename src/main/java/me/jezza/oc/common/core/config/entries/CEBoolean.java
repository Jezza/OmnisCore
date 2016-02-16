package me.jezza.oc.common.core.config.entries;

import static me.jezza.oc.common.utils.helpers.StringHelper.useable;

import java.lang.reflect.Field;

import me.jezza.oc.common.core.config.Config.ConfigBoolean;
import me.jezza.oc.common.core.config.ConfigEntry;
import me.jezza.oc.common.core.config.OmnisConfiguration;
import me.jezza.oc.common.core.config.discovery.AnnotatedField;
import me.jezza.oc.common.interfaces.InputBuffer;
import me.jezza.oc.common.interfaces.OutputBuffer;
import me.jezza.oc.common.utils.Classes;
import net.minecraft.entity.player.EntityPlayer;

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
	public Boolean load(OmnisConfiguration config, AnnotatedField<ConfigBoolean, Boolean> field) {
		ConfigBoolean annotation = field.annotation();
		return config.getBoolean(annotation.category(), field.name(), field.defaultValue(), processComment(annotation.comment()));
	}

	@Override
	public void save(OmnisConfiguration config, AnnotatedField<ConfigBoolean, Boolean> field) {
		ConfigBoolean annotation = field.annotation();
		config.getBooleanProperty(annotation.category(), field.name(), field.defaultValue(), processComment(annotation.comment())).set(field.currentValue());
	}

	@Override
	protected void writeField(EntityPlayer player, OutputBuffer buffer, AnnotatedField<ConfigBoolean, Boolean> field) {
		buffer.writeBoolean(field.currentValue());
	}

	@Override
	protected Boolean readField(InputBuffer buffer, AnnotatedField<ConfigBoolean, Boolean> field) {
		return buffer.readBoolean();
	}
}