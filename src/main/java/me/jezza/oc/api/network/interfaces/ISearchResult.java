package me.jezza.oc.api.network.interfaces;

import java.util.List;

public interface ISearchResult<T extends INetworkNode<T>> {

	/**
	 * @return true if the search has successfully discovered a path.
	 */
	public boolean hasFinished();

	/**
	 * @return the path that was discovered, else null.
	 */
	public List<T> getPath();

}
