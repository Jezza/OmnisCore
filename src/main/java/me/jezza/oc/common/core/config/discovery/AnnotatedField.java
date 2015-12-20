package me.jezza.oc.common.core.config.discovery;

import com.google.common.base.Throwables;
import me.jezza.oc.OmnisCore;
import me.jezza.oc.common.core.config.Config.ConfigSync;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import static me.jezza.oc.common.utils.helpers.StringHelper.convertToLowerSnakeCase;

/**
 * @author Jezza
 */
public class AnnotatedField<T extends Annotation, V> {
	protected final Field field;
	protected final T annotation;
	protected final V defaultValue;
	protected final String fieldName;
	protected transient Boolean sync = null;

	/**
	 * @param field      A static field
	 * @param annotation The annotation on the field
	 */
	public AnnotatedField(Field field, T annotation) {
		this.field = field;
		this.fieldName = convertToLowerSnakeCase(field.getName());
		this.annotation = annotation;
		this.defaultValue = currentValue();
	}

	public final Field field() {
		return field;
	}

	public final T annotation() {
		return annotation;
	}

	public final V defaultValue() {
		return defaultValue;
	}

	public final String fieldName() {
		return fieldName;
	}

	/**
	 * @return The value currently stored in the field, can be null.
	 */
	@SuppressWarnings("unchecked")
	public final V currentValue() {
		try {
			return (V) field.get(null);
		} catch (IllegalAccessException e) {
			OmnisCore.logger.error("Failed to retrieve current value.");
			throw Throwables.propagate(e);
		} catch (ClassCastException e) {
			OmnisCore.logger.error("Field with: {} not of expected type: {}", annotation.annotationType(), field.toGenericString());
			throw Throwables.propagate(e);
		}
	}

	public boolean canSync() {
		if (sync == null)
			sync = field.isAnnotationPresent(ConfigSync.class);
		return sync;
	}

	@Override
	public final String toString() {
		return field.getDeclaringClass().getCanonicalName() + '.' + fieldName + ", @" + annotation.annotationType().getSimpleName() + ", sync: " + canSync();
	}
}
