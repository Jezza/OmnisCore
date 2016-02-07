package me.jezza.oc.common.interfaces;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static me.jezza.oc.common.core.command.CommandAbstract.DEFAULT_JOINER;
import static me.jezza.oc.common.core.command.CommandAbstract.DEFAULT_SPLITTER;

/**
 * TODO This shit.
 *
 * @author Jezza
 */
@Target({METHOD, TYPE})
@Retention(RUNTIME)
public @interface Command {
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
}
