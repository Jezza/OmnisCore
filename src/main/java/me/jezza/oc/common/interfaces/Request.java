package me.jezza.oc.common.interfaces;

/**
 * @author Jezza
 */
public interface Request {
	/**
	 * Used to tell the controller that you no longer wish for the request to be fulfilled.
	 * NOTE: the general implementation is that if the request has been fulfilled and waiting for {@link #acquire()}, shouldn't
	 */
	void cancel();

	boolean acquired();

	void release();
}
