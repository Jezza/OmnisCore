package me.jezza.oc.common.interfaces;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @author Jezza
 */
@Target({METHOD, TYPE})
@Retention(RUNTIME)
public @interface Command {
	String DEFAULT_JOINER = " ";
	String DEFAULT_SPLITTER = "|";

	int NONE = 0x0;
	int CLIENT_SIDE = 0x1;
	int SERVER_SIDE = 0x2;
	int BOTH_SIDES = CLIENT_SIDE | SERVER_SIDE;

	int DEFAULT_SIDES = BOTH_SIDES;

	/**
	 * @return
	 */
	String value();

	/**
	 * @return
	 */
	String nameSplitter() default DEFAULT_SPLITTER;

	/**
	 * @return
	 */
	String joiner() default DEFAULT_JOINER;

	int side() default DEFAULT_SIDES;
}
