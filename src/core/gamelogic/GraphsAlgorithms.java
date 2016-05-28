package core.gamelogic;

import core.Player;
import core.gamemodel.Town;
import core.gamemodel.TownName;

import java.util.*;

/**
 * Created by Leonardo Arcari on 25/05/2016.
 */
public class GraphsAlgorithms {
    public static List<Town> townsWithEmporiumOf(Map<TownName, Town> graph, TownName source, Player player) {
        if(source == null || graph == null) throw new IllegalArgumentException();

        Map<TownName, Boolean> marked = new HashMap<>(graph.size());
        List<Town> returnList = new ArrayList<>();

        for (TownName townName : graph.keySet())  {
            marked.put(townName, false);
        }
        marked.put(source, true);

        ArrayDeque<TownName> deque = new ArrayDeque<>();
        deque.addFirst(source);
        while (!deque.isEmpty()) {
            TownName node = deque.removeLast();
            Iterator<TownName> nearbiesIterator = graph.get(node).nearbiesIterator();
            while (nearbiesIterator.hasNext()) {
                TownName nearbyNode = nearbiesIterator.next();
                if (!marked.get(nearbyNode) && graph.get(nearbyNode).hasEmporium(player)) {
                    marked.put(nearbyNode, true);
                    returnList.add(graph.get(nearbyNode));
                    deque.addFirst(nearbyNode);
                }
            }
            marked.put(node, true);
        }
        return returnList;
    }

    public static Map<TownName, Integer> reachableTowns(Map<TownName, Town> graph, TownName source, int coins) {
        if(source == null || graph == null) throw new IllegalArgumentException();
        Map<TownName, Boolean> marked = new HashMap<>(graph.size());
        Map<TownName, Integer> returnMap = new HashMap<>();

        for (TownName townName : graph.keySet())  {
            marked.put(townName, false);
        }
        marked.put(source, true);
        returnMap.put(source, 0);

        ArrayDeque<TownName> deque = new ArrayDeque<>();
        deque.addFirst(source);
        while (!deque.isEmpty()) {
            TownName node = deque.removeLast();
            Iterator<TownName> nearbiesIterator = graph.get(node).nearbiesIterator();
            while (nearbiesIterator.hasNext()) {
                TownName nearbyNode = nearbiesIterator.next();
                int cost = returnMap.get(node) + 2;
                if (!marked.get(nearbyNode) && cost <= coins) {
                    marked.put(nearbyNode, true);
                    returnMap.put(nearbyNode, cost);
                    deque.addFirst(nearbyNode);
                }
            }
            marked.put(node, true);
        }
        return returnMap;
    }
}
