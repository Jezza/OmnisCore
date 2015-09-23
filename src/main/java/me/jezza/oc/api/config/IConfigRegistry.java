package me.jezza.oc.api.config;

import me.jezza.oc.common.core.config.ConfigEntry;

import java.lang.annotation.Annotation;

/**
 * This interface is the only interaction you should have/need with the ConfigAnnotation system.
 * If you feel that this interface doesn't provide the functionality you're looking for, give me a shout.
 */
public interface IConfigRegistry {
    /**
     * To use this, implement {@link me.jezza.oc.api.config.Config.IConfigRegistrar} on your main mod file.
     *
     * @return true - if the configEntry was successfully registered, false otherwise
     */
    <A extends Annotation, T extends ConfigEntry<A, ?>> boolean registerAnnotation(Class<A> clazz, Class<T> configEntry);
}
