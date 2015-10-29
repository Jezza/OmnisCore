package me.jezza.oc.common.core.config;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;

/**
 * @author Jezza
 */
public interface ICEFactory<A extends Annotation, T extends ConfigEntry<A, ?>> {

	T create(Object... params) throws InstantiationException, IllegalAccessException, InvocationTargetException;

	Class<A> annotationClass();
}
