package me.jezza.oc.api.configuration.lib;

import me.jezza.oc.api.configuration.ConfigEntry;

import java.lang.annotation.Annotation;

public interface IConfigRegistry {
    /**
     * Only call this when you've had the chance from the interface.
     * To use this, implement {@link me.jezza.oc.api.configuration.Config.IConfigRegistrar} on your main mod file.
     *
     * @return true - if the configEntry was successfully registered, false otherwise
     */
    <A extends Annotation, T extends ConfigEntry<A, ?>> boolean registerAnnotation(Class<A> clazz, Class<T> configEntry);
}
