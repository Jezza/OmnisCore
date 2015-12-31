package me.jezza.oc.common.interfaces;

/**
 * @author Jezza
 */
public interface ResourceRequest<T extends Resource> extends Request {
	T acquire();
}
