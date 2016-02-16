package me.jezza.oc.common.core.config.entries;

import static me.jezza.oc.common.utils.helpers.StringHelper.useable;

import java.lang.reflect.Field;

import me.jezza.oc.common.core.config.Config.ConfigFloat;
import me.jezza.oc.common.core.config.ConfigEntry;
import me.jezza.oc.common.core.config.OmnisConfiguration;
import me.jezza.oc.common.core.config.discovery.AnnotatedField;
import me.jezza.oc.common.interfaces.InputBuffer;
import me.jezza.oc.common.interfaces.OutputBuffer;
import me.jezza.oc.common.utils.Classes;
import net.minecraft.entity.player.EntityPlayer;

public class CEFloat extends ConfigEntry<ConfigFloat, Float> {
	public CEFloat(OmnisConfiguration config) {
		super(config);
	}

	@Override
	protected boolean checkField(Field field) {
		return Classes.unwrap(field.getType()) == float.class;
	}

	@Override
	protected String fieldName(Field field, ConfigFloat annotation) {
		String name = annotation.name();
		return useable(name) ? name : super.fieldName(field, annotation);
	}

	@Override
	protected Float load(OmnisConfiguration config, AnnotatedField<ConfigFloat, Float> field) {
		ConfigFloat annotation = field.annotation();
		String comment = processComment(annotation.comment());
		return config.getFloat(annotation.category(), field.name(), field.defaultValue(), annotation.minValue(), annotation.maxValue(), comment);
	}

	@Override
	protected void save(OmnisConfiguration config, AnnotatedField<ConfigFloat, Float> field) {
		ConfigFloat annotation = field.annotation();
		String comment = processComment(annotation.comment());
		config.getFloatProperty(annotation.category(), field.name(), field.defaultValue(), annotation.minValue(), annotation.maxValue(), comment).set(field.currentValue());
	}

	@Override
	protected void writeField(EntityPlayer player, OutputBuffer buffer, AnnotatedField<ConfigFloat, Float> field) {
		buffer.writeFloat(field.currentValue());
	}

	@Override
	protected Float readField(InputBuffer buffer, AnnotatedField<ConfigFloat, Float> field) {
		return buffer.readFloat();
	}
}
