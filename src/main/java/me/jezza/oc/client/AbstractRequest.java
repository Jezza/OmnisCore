package me.jezza.oc.client;

import me.jezza.oc.common.interfaces.Request;

import java.util.concurrent.CancellationException;

/**
 * @author Jezza
 */
public abstract class AbstractRequest implements Request {
	private boolean acquired;
	private boolean retrieved;
	private boolean cancelled;
	private boolean released;

	@Override
	public final boolean acquired() {
		return acquired && !retrieved;
	}

	@Override
	public final void cancel() {
		this.cancelled = true;
	}

	protected final boolean retrieved() {
		return retrieved;
	}

	protected final boolean cancelled() {
		return cancelled;
	}

	protected final void acquired(boolean acquired) {
		this.acquired = acquired;
	}

	@Override
	public final void acquire() {
		if (released)
			throw new IllegalStateException("Request has already been released.");
		if (cancelled)
			throw new CancellationException();
		if (!retrieved) {
			retrieved = true;
			onAcquisition();
		}
	}

	@Override
	public final void release() {
		released = true;
		onRelease();
	}

	protected abstract void onAcquisition();

	protected abstract void onRelease();
}
