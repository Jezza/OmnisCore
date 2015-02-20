package me.jezza.oc.api.network.search;

import me.jezza.oc.api.network.interfaces.INetworkNode;
import me.jezza.oc.api.network.interfaces.ISearchPattern;
import me.jezza.oc.api.network.interfaces.ISearchResult;
import me.jezza.oc.common.core.CoreProperties;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.LinkedBlockingDeque;

public class SearchThread extends Thread {

    private static final SearchThread INSTANCE = new SearchThread();

    private SearchThread() {
        super(CoreProperties.MOD_ID + "|NST");
    }

    private static final LinkedBlockingDeque<ISearchPattern> searchPatterns = new LinkedBlockingDeque<ISearchPattern>(256);
    private static final ArrayList<ISearchPattern> completedPatterns = new ArrayList<ISearchPattern>(32);

    @Override
    public void run() {
        ISearchPattern pattern;
        int count, size;

        while (true) {
            // Active search patterns.
            count = -1;
            size = searchPatterns.size();
            while (count < size) {
                try {
                    pattern = searchPatterns.takeFirst();
                } catch (InterruptedException e) {
                    continue;
                }

                boolean foundPath = pattern.searchForPath();

                if (foundPath)
                    completedPatterns.add(pattern);
                else
                    searchPatterns.offerLast(pattern);

                count++;
            }

            // Check completed patterns for deletion.
            Iterator<ISearchPattern> iterator = completedPatterns.iterator();
            while (iterator.hasNext())
                if (iterator.next().canDelete())
                    iterator.remove();
        }
    }

    public static SearchThread getInstance() {
        return INSTANCE;
    }

    public static ISearchResult addSearchPattern(INetworkNode startNode, INetworkNode endNode, Map<? extends INetworkNode, ? extends Collection<INetworkNode>> nodeMap) {
        BFSPattern pattern = new BFSPattern(startNode, endNode, nodeMap);
        searchPatterns.offer(pattern);
        return pattern;
    }
}
