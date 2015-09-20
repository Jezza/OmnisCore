package me.jezza.oc.api.configuration.lib;

import me.jezza.oc.api.configuration.ConfigEntry;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;

/**
 * @author Jezza
 */
public interface ICEFactory<A extends Annotation, T extends ConfigEntry<A, ?>> {

    T create(Object... params) throws InstantiationException, IllegalAccessException, InvocationTargetException;

    Class<A> annotationClazz();

}
