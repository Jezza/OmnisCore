package me.jezza.oc.common.utils.helpers;

import com.google.common.base.Predicate;

/**
 * @author Jezza
 */
public class PredicateHelper {

	public PredicateHelper() {
		throw new IllegalStateException();
	}

	public static Predicate<String> startsWith(final String with) {
		return new Predicate<String>() {
			@Override
			public boolean apply(String s) {
				return s.startsWith(with);
			}
		};
	}

	public static Predicate<String> endsWith(final String with) {
		return new Predicate<String>() {
			@Override
			public boolean apply(String s) {
				return s.endsWith(with);
			}
		};
	}
}
