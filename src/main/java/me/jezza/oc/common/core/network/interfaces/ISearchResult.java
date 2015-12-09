package me.jezza.oc.common.core.network.interfaces;

import java.util.List;

public interface ISearchResult<T extends INetworkNode<T>> {

	/**
	 * @return true if the search has successfully discovered a path.
	 */
	boolean finished();

	/**
	 * @return the path that was discovered, else null.
	 */
	List<T> path();

}
