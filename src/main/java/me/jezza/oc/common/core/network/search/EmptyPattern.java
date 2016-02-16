package me.jezza.oc.common.core.network.search;

import java.util.List;

import me.jezza.oc.common.core.network.interfaces.INetworkNode;
import me.jezza.oc.common.core.network.interfaces.ISearchPattern;

public class EmptyPattern implements ISearchPattern {

	private boolean delete = false;

	@Override
	public boolean search() {
		return true;
	}

	@Override
	public boolean canDelete() {
		return delete;
	}

	@Override
	public boolean finished() {
		return true;
	}

	@Override
	public List<INetworkNode> path() {
		delete = true;
		return null;
	}
}
