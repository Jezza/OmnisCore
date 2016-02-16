package me.jezza.oc.common.core.config.entries;

import static me.jezza.oc.common.utils.helpers.StringHelper.useable;

import java.lang.reflect.Field;

import me.jezza.oc.common.core.config.Config.ConfigInteger;
import me.jezza.oc.common.core.config.ConfigEntry;
import me.jezza.oc.common.core.config.OmnisConfiguration;
import me.jezza.oc.common.core.config.discovery.AnnotatedField;
import me.jezza.oc.common.interfaces.InputBuffer;
import me.jezza.oc.common.interfaces.OutputBuffer;
import me.jezza.oc.common.utils.Classes;
import net.minecraft.entity.player.EntityPlayer;

public class CEInteger extends ConfigEntry<ConfigInteger, Integer> {
	public CEInteger(OmnisConfiguration config) {
		super(config);
	}

	@Override
	protected boolean checkField(Field field) {
		return Classes.unwrap(field.getType()) == int.class;
	}

	@Override
	protected String fieldName(Field field, ConfigInteger annotation) {
		String name = annotation.name();
		return useable(name) ? name : super.fieldName(field, annotation);
	}

	@Override
	protected Integer load(OmnisConfiguration config, AnnotatedField<ConfigInteger, Integer> field) {
		ConfigInteger annotation = field.annotation();
		String comment = processComment(annotation.comment());
		return config.getInt(annotation.category(), field.name(), field.defaultValue(), annotation.minValue(), annotation.maxValue(), comment);
	}

	@Override
	protected void save(OmnisConfiguration config, AnnotatedField<ConfigInteger, Integer> field) {
		ConfigInteger annotation = field.annotation();
		String comment = processComment(annotation.comment());
		config.getIntProperty(annotation.category(), field.name(), field.defaultValue(), annotation.minValue(), annotation.maxValue(), comment).set(field.currentValue());
	}

	@Override
	protected void writeField(EntityPlayer player, OutputBuffer buffer, AnnotatedField<ConfigInteger, Integer> field) {
		buffer.writeInt(field.currentValue());
	}

	@Override
	protected Integer readField(InputBuffer buffer, AnnotatedField<ConfigInteger, Integer> field) {
		return buffer.readInt();
	}
}
