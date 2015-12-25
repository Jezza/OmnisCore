package me.jezza.oc.common.interfaces;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to register a {@link IOmnisPacket}.
 * The value is splitted on the return value from splitter().
 * The values are then treated as mod ids, and the class is registered with those specific channels.
 *
 * @author Jezza
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ModPacket {
	String DEFAULT_SPLITTER = ":";

	/**
	 * @return - the ModID(s) used to register the class.
	 */
	String value();

	String splitter() default DEFAULT_SPLITTER;
}
