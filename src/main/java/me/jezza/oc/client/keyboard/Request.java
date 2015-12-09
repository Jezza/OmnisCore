package me.jezza.oc.client.keyboard;

import java.util.concurrent.CancellationException;

/**
 * @author Jezza
 */
public class Request<T> {
	protected T value;
	protected boolean cancel;

	public boolean cancel() {
		return cancel;
	}

	public boolean isCancelled() {
		return cancel;
	}

	public boolean acquired() {
		return value != null;
	}

	public void set(T value) {
		this.value = value;
	}

	public T get() {
		if (cancel)
			throw new CancellationException();
		onRetrieval();
		return value;
	}

	protected void onRetrieval() {
	}
}
