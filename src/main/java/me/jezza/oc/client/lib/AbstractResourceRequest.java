package me.jezza.oc.client.lib;

import me.jezza.oc.common.interfaces.Resource;
import me.jezza.oc.common.interfaces.ResourceRequest;

/**
 * @author Jezza
 */
public abstract class AbstractResourceRequest<T extends Resource> extends AbstractRequest implements ResourceRequest<T> {
	private T resource;

	protected AbstractResourceRequest() {
	}

	@Override
	public boolean acquired() {
		return resource != null;
	}

	protected void acquired(T resource) {
		this.resource = resource;
	}

	@Override
	public T acquire() {
		confirmAcquisition();
		return resource;
	}

	@Override
	public void release() {
		super.release();
		resource.invalidate();
		resource = null;
	}
}
