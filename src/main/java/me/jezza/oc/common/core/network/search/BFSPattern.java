package me.jezza.oc.common.core.network.search;

import me.jezza.oc.common.core.network.interfaces.INetworkNode;
import me.jezza.oc.common.core.network.interfaces.ISearchPattern;

import java.util.*;

public class BFSPattern<T extends INetworkNode<T>> implements ISearchPattern<T> {

	private Map<? extends T, ? extends Collection<T>> nodeMap;
	private T startNode, endNode;
	private boolean finished, deletable;

	private List<T> path;

	public BFSPattern(T startNode, T endNode, Map<? extends T, ? extends Collection<T>> nodeMap) {
		finished = deletable = false;
		this.startNode = startNode;
		this.endNode = endNode;
		this.nodeMap = nodeMap;
	}

	@Override
	public synchronized boolean canDelete() {
		return deletable;
	}

	@Override
	public synchronized boolean hasFinished() {
		return finished;
	}

	@Override
	public boolean searchForPath() {
		if (startNode.equals(endNode)) {
			finished = true;
			return true;
		}

		Map<T, T> linkMap = new LinkedHashMap<>();
		ArrayList<T> visited = new ArrayList<>();

		Deque<T> deque = new ArrayDeque<>();
		deque.offer(startNode);

		while (deque.size() > 0) {
			T node = deque.poll();
			visited.add(node);

			if (node.equals(endNode)) {
				path = new ArrayList<T>();
				T nextNode = endNode;
				while (!nextNode.equals(startNode)) {
					path.add(nextNode);
					nextNode = linkMap.get(nextNode);
				}
				Collections.reverse(path);
				finished = true;
				return true;
			} else {
				Collection<T> nodes = nodeMap.get(node);
				for (T childNode : nodes) {
					if (visited.contains(childNode))
						continue;
					linkMap.put(childNode, node);
					deque.offer(childNode);
				}
			}
		}

		finished = true;
		// I'm not setting the path to even an emptyList because when it's set to null, this will at least show that no path was found.
		return false;
	}

	@Override
	public List<T> getPath() {
		if (!finished || path == null || path.isEmpty())
			return Collections.<T>emptyList();
		deletable = true;
		return path;
	}
}
