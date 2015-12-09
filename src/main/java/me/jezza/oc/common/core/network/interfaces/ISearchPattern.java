package me.jezza.oc.common.core.network.interfaces;

public interface ISearchPattern<T extends INetworkNode<T>> extends ISearchResult<T> {

	/**
	 * @return true if path was found.
	 */
	boolean search();

	/**
	 * Used by the search thread to determine if the pattern can be deleted.
	 *
	 * @return true if it can be deleted.
	 */
	boolean canDelete();

}
