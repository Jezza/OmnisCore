package me.jezza.oc.common.interfaces;

/**
 * @author Jezza
 */
public interface Copyable<T> extends Cloneable {
	T copy();
}
