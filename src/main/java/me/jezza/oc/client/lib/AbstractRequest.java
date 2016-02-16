package me.jezza.oc.client.lib;

import java.util.concurrent.CancellationException;

import me.jezza.oc.common.interfaces.Request;

/**
 * @author Jezza
 */
public abstract class AbstractRequest implements Request {
	protected boolean retrieved;
	protected boolean cancelled;
	protected boolean released;

	protected AbstractRequest() {
	}

	@Override
	public void cancel() {
		this.cancelled = true;
	}

	public boolean cancelled() {
		return cancelled;
	}

	public boolean retrieved() {
		return retrieved;
	}

	public boolean released() {
		return released;
	}

	@Override
	public void release() {
		if (released)
			throw new IllegalStateException("Request as already been released.");
		released = true;
		onRelease();
	}

	protected boolean confirmAcquisition() {
		if (!acquired())
			throw new IllegalStateException("Request has not been acquired.");
		if (released)
			throw new IllegalStateException("Request has already been released.");
		if (cancelled)
			throw new CancellationException();
		if (retrieved)
			return false;
		retrieved = true;
		onAcquisition();
		return true;
	}

	protected abstract void onRelease();

	protected abstract void onAcquisition();
}
