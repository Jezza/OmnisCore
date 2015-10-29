package me.jezza.oc.common.core.network.search;

import me.jezza.oc.common.core.network.interfaces.INetworkNode;
import me.jezza.oc.common.core.network.interfaces.ISearchPattern;

import java.util.List;

public class EmptyPattern implements ISearchPattern {

	private boolean delete = false;

	@Override
	public boolean searchForPath() {
		return true;
	}

	@Override
	public boolean canDelete() {
		return delete;
	}

	@Override
	public boolean hasFinished() {
		return true;
	}

	@Override
	public List<INetworkNode> getPath() {
		delete = true;
		return null;
	}
}
