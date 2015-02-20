package me.jezza.oc.api.network.search;

import me.jezza.oc.api.network.interfaces.INetworkNode;
import me.jezza.oc.api.network.interfaces.ISearchPattern;

import java.util.*;

public class BFSPattern implements ISearchPattern {

    private Map<? extends INetworkNode, ? extends Collection<INetworkNode>> nodeMap;
    private INetworkNode startNode, endNode;
    private boolean finished, deletable;

    private List<INetworkNode> path;

    public BFSPattern(INetworkNode startNode, INetworkNode endNode, Map<? extends INetworkNode, ? extends Collection<INetworkNode>> nodeMap) {
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

//        Map<INetworkNode, ForgeDirection> map = new LinkedHashMap<INetworkNode, ForgeDirection>();
        Map<INetworkNode, INetworkNode> linkMap = new LinkedHashMap<INetworkNode, INetworkNode>();
        ArrayList<INetworkNode> visited = new ArrayList<INetworkNode>();

        Deque<INetworkNode> deque = new LinkedList<INetworkNode>();
        deque.offer(startNode);

        while (deque.size() > 0) {
            INetworkNode node = deque.poll();
            visited.add(node);

            if (node.equals(endNode)) {
                path = new ArrayList<INetworkNode>();
                INetworkNode nextNode = endNode;
                while (!nextNode.equals(startNode)) {
                    path.add(nextNode);
                    nextNode = linkMap.get(nextNode);
                }
                Collections.reverse(path);
                finished = true;
                return true;
            } else {
                Collection<INetworkNode> nodes = nodeMap.get(node);
                for (INetworkNode childNode : nodes) {
                    if (visited.contains(childNode))
                        continue;
                    linkMap.put(childNode, node);
                    deque.offer(childNode);
                }
//                for (Map.Entry<ForgeDirection, INetworkNode> entry : node.getDirectionalMap().entrySet()) {
//                    INetworkNode childNode = entry.getValue();
//                    if (visited.contains(childNode))
//                        continue;
//                    map.put(childNode, entry.getKey().getOpposite());
//                    deque.offer(childNode);
//                }
            }
        }

        return false;
    }

    @Override
    public List<INetworkNode> getPath() {
        if (!finished || path == null || path.isEmpty())
            return Collections.<INetworkNode>emptyList();
        deletable = true;
        return path;
    }
}
