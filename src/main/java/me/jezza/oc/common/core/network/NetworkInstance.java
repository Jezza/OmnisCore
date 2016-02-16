package me.jezza.oc.common.core.network;

import static me.jezza.oc.common.core.network.NetworkResponse.*;

import java.util.*;

import cpw.mods.fml.common.FMLCommonHandler;
import me.jezza.oc.common.core.network.exceptions.NetworkAddException;
import me.jezza.oc.common.core.network.exceptions.NetworkCreationException;
import me.jezza.oc.common.core.network.exceptions.NetworkException;
import me.jezza.oc.common.core.network.exceptions.NetworkRemoveException;
import me.jezza.oc.common.core.network.interfaces.INetworkNode;
import me.jezza.oc.common.core.network.interfaces.INetworkNodeHandler;

/**
 * This is the master instance.
 * Used to add and remove nodes to your network.
 */
public class NetworkInstance<T extends INetworkNode<T>> {
	private List<INetworkNodeHandler<T>> networks;
	private Class<? extends INetworkNodeHandler<T>> nodeHandlerClass;

	@SuppressWarnings("unchecked")
	public NetworkInstance() {
		this((Class) NetworkCore.class);
	}

	public NetworkInstance(Class<? extends INetworkNodeHandler<T>> nodeHandlerClass) {
		this.networks = new ArrayList<>();
		this.nodeHandlerClass = nodeHandlerClass;
	}

	public NodeAdded addNetworkNode(T node) throws NetworkException {
		List<INetworkNodeHandler<T>> networksFound = new ArrayList<>();
		Collection<T> nearbyNodes = node.getNearbyNodes();

		if (!nearbyNodes.isEmpty())
			nodeIterator:
					for (T nearbyNode : nearbyNodes)
						for (INetworkNodeHandler<T> networkFound : networks)
							if (!networksFound.contains(networkFound))
								if (networkFound.containsNode(nearbyNode)) {
									networksFound.add(networkFound);
									continue nodeIterator;
								}

		INetworkNodeHandler<T> networkNodeHandler;
		NodeAdded response;
		switch (networksFound.size()) {
			case 0:
				response = NodeAdded.NETWORK_CREATION;
				networkNodeHandler = createNodeHandler();
				break;
			case 1:
				response = NodeAdded.NETWORK_JOIN;
				networkNodeHandler = networksFound.get(0);
				break;
			default:
				response = NodeAdded.NETWORK_MERGE;
				networkNodeHandler = networksFound.get(0);
				List<INetworkNodeHandler<T>> nodes = networksFound.subList(1, networksFound.size());
				for (INetworkNodeHandler<T> networkFound : nodes) {
//                    networkNodeHandler.mergeNetwork(networkFound.getNodeMap());
					removeNetworkNodeHandler(networkFound);
				}
		}

		boolean flag = networkNodeHandler.addNetworkNode(node);
		if (!flag) {
			if (response == NodeAdded.NETWORK_CREATION)
				removeNetworkNodeHandler(networkNodeHandler);

			throw new NetworkAddException("Failed to add node to network. Node: %s", node.getClass());
		}
		return response;
	}

	public NodeRemoved removeNetworkNode(T node) throws NetworkException {
		INetworkNodeHandler<T> nodeHandler = null;
		for (INetworkNodeHandler<T> networkNodeHandler : networks) {
			if (networkNodeHandler.containsNode(node)) {
				nodeHandler = networkNodeHandler;
				break;
			}
		}

		if (nodeHandler == null)
			return NodeRemoved.NETWORK_NOT_FOUND;

		Map<? extends T, ? extends Collection<T>> nodeMap = nodeHandler.getNodeMap();

		boolean removed = nodeHandler.removeNetworkNode(node);
		if (!removed)
			throw new NetworkRemoveException("Failed to remove node from network. Node: %s", node.getClass());

		Collection<T> nearbyNodes = nodeMap.get(node);
		switch (nearbyNodes.size()) {
			case 0:
				removeNetworkNodeHandler(nodeHandler);
				return NodeRemoved.NETWORK_DESTROYED;
			case 1:
				return NodeRemoved.NETWORK_LEAVE;
			default:
				Iterator<T> nextNode = nearbyNodes.iterator();

				Collection<T> connectedNodes = breadthFirstSearchSpread(nextNode.next(), nodeMap);

				int nodeHandlerSize = nodeHandler.size();
				if (connectedNodes.size() == nodeHandlerSize)
					return NodeRemoved.NETWORK_LEAVE;

				HashSet<T> visited = new HashSet<>(connectedNodes);
				visited.add(node);
				nodeHandler.retainAll(connectedNodes);

				Set<? extends T> keySet = nodeMap.keySet();
				while (visited.size() != keySet.size()) {
					if (!nextNode.hasNext())
						break;
					T networkNode = nextNode.next();
					if (visited.contains(networkNode))
						continue;
					connectedNodes = breadthFirstSearchSpread(networkNode, nodeMap);
					visited.addAll(connectedNodes);
					createNodeHandlerAndFill(connectedNodes);
				}
		}

		return NodeRemoved.NETWORK_SPLIT;
	}

	public NodeUpdated updateNetworkNode(T node) throws NetworkException {
		INetworkNodeHandler<T> nodeHandler = null;
		for (INetworkNodeHandler<T> networkNodeHandler : networks)
			if (networkNodeHandler.containsNode(node)) {
				nodeHandler = networkNodeHandler;
				break;
			}

		if (nodeHandler == null)
			return NodeUpdated.NETWORK_NOT_FOUND;

		Map<? extends T, ? extends Collection<T>> nodeMap = nodeHandler.getNodeMap();
		Collection<T> cachedNodes = nodeMap.get(node);
		Collection<T> currentNodes = node.getNearbyNodes();

		if (cachedNodes.equals(currentNodes))
			return NodeUpdated.NETWORK_NO_DELTA_DETECTED;

		boolean removed = nodeHandler.removeNetworkNode(node);
		if (!removed)
			throw new NetworkRemoveException("Failed to remove node from network. Node: %s", node.getClass());

		Collection<T> nearbyNodes = nodeMap.get(node);
		switch (nearbyNodes.size()) {
			case 0:
				removeNetworkNodeHandler(nodeHandler);
			case 1:
				break;
			default:
				Iterator<T> nextNode = nearbyNodes.iterator();

				Collection<T> connectedNodes = breadthFirstSearchSpread(nextNode.next(), nodeMap);

				int nodeHandlerSize = nodeHandler.size();
				if (connectedNodes.size() == nodeHandlerSize)
					break;

				HashSet<T> visited = new HashSet<>(connectedNodes);
				visited.add(node);
				nodeHandler.retainAll(connectedNodes);

				Set<? extends T> keySet = nodeMap.keySet();
				while (visited.size() != keySet.size()) {
					if (!nextNode.hasNext())
						break;
					T networkNode = nextNode.next();
					if (visited.contains(networkNode))
						continue;
					connectedNodes = breadthFirstSearchSpread(networkNode, nodeMap);
					visited.addAll(connectedNodes);
					createNodeHandlerAndFill(connectedNodes);
				}
		}

		addNetworkNode(node);
		return NodeUpdated.NETWORK_UPDATED;
	}

	private INetworkNodeHandler<T> createNodeHandler() throws NetworkCreationException {
		INetworkNodeHandler<T> nodeHandler;
		try {
			nodeHandler = nodeHandlerClass.newInstance();
		} catch (Exception e) {
			throw new NetworkCreationException(e, "Failed to created INetworkNodeHandler. Class %s", nodeHandlerClass);
		}
		if (nodeHandler.requiresRegistration())
			FMLCommonHandler.instance().bus().register(nodeHandler);
		networks.add(nodeHandler);
		return nodeHandler;
	}

	private INetworkNodeHandler<T> createNodeHandlerAndFill(Collection<T> collection) throws NetworkCreationException {
		INetworkNodeHandler<T> nodeHandler = createNodeHandler();
		for (T node : collection)
			nodeHandler.addNetworkNode(node);
		return nodeHandler;
	}

	private void removeNetworkNodeHandler(INetworkNodeHandler<T> networkCore) {
		networkCore.destroy();
		if (networkCore.requiresRegistration())
			FMLCommonHandler.instance().bus().unregister(networkCore);
		networks.remove(networkCore);
	}

	/**
	 * @param startingNode - Starting node.
	 * @return A collection of all connected nodes.
	 */
	public Collection<T> breadthFirstSearchSpread(T startingNode, Map<? extends INetworkNode<T>, ? extends Collection<T>> nodeMap) {
		HashSet<T> visited = new HashSet<>();
		Queue<T> queue = new LinkedList<>();
		queue.offer(startingNode);

		while (!queue.isEmpty()) {
			T node = queue.poll();
			visited.add(node);
			Collection<T> nearbyNodes = nodeMap.get(node);
			for (T childNode : nearbyNodes)
				if (!visited.contains(childNode))
					queue.offer(childNode);
		}
		return visited;
	}
}
