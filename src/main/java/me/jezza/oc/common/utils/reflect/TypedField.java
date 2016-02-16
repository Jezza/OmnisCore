package me.jezza.oc.common.utils.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;

import cpw.mods.fml.relauncher.ReflectionHelper.UnableToFindFieldException;
import me.jezza.oc.common.utils.helpers.StringHelper;
import org.apache.http.util.Args;
import sun.reflect.CallerSensitive;

/**
 * @author Jezza
 */
public class TypedField<T> {
	private final Class<T> type;
	private final Field field;

	public TypedField(Class<T> type, Field field) {
		Args.check(field.getType() == type, StringHelper.format("Field {} is not of expected type: {}", field, type));
		this.type = type;
		this.field = field;
	}

	public String name() {
		return raw().getName();
	}

	public Class<T> type() {
		return type;
	}

	public int modifiers() {
		return raw().getModifiers();
	}

	public boolean enumConstant() {
		return raw().isEnumConstant();
	}

	public boolean synthetic() {
		return raw().isSynthetic();
	}

	public Class<?> declaredType() {
		return raw().getType();
	}

	public Class<?> declaringClass() {
		return raw().getDeclaringClass();
	}

	public <A extends Annotation> A annotation(Class<A> annotationClass) {
		return raw().getAnnotation(annotationClass);
	}

	public Annotation[] declaredAnnotations() {
		return raw().getDeclaredAnnotations();
	}

	public Type genericType() {
		return raw().getGenericType();
	}

	@CallerSensitive
	public T get(Object obj) throws IllegalArgumentException, IllegalAccessException, ClassCastException {
		return type.cast(raw().get(obj));
	}

	@CallerSensitive
	public void set(Object obj, T value) throws IllegalArgumentException, IllegalAccessException {
		raw().set(obj, value);
	}

	public final Field raw() {
		return field;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof TypedField) {
			TypedField<?> other = (TypedField) obj;
			return type() == other.type() && declaringClass() == other.declaringClass() && (name() == other.name()) && (declaredType() == other.declaredType());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return declaringClass().getName().hashCode() ^ name().hashCode();
	}

	public String toGenericString() {
		return raw().toGenericString();
	}

	@Override
	public String toString() {
		return raw().toString();
	}

	public static <T> TypedField<T> find(Class<?> clazz, Class<T> type, String... fieldNames) {
		Exception failed = null;
		for (String fieldName : fieldNames) {
			try {
				Field f = clazz.getDeclaredField(fieldName);
				if (f.getType() == type) {
					f.setAccessible(true);
					return new TypedField<>(type, f);
				}
			} catch (Exception e) {
				failed = e;
			}
		}
		throw new UnableToFindFieldException(fieldNames, failed);
	}
}
