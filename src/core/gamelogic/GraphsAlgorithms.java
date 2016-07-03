package core.gamelogic;

import core.Player;
import core.gamemodel.Town;
import core.gamemodel.TownName;
import core.gamemodel.modelinterface.TownInterface;

import java.util.*;

/**
 * Utility class providing algorithms for graphs search on a game map.
 */
public class GraphsAlgorithms {
    /**
     * Graph search algorithm where the Game map is the input graph. Given <code>graph, source and
     * player</code> the algorithm builds a list of {@link Town Towns} such that <code>list.contains(t) &lt;==&gt;
     * t.hasEmporium(player) &amp;&amp; (* there exists a path in the graph from source to t *)</code>. The algorithm is
     * implemented with a modified version of the BFS algorithm from <i>Introduction to Algorithms by Thomas H. Cormen,
     * Charles E. Leiserson, Ronald L. Rivest e Clifford Stein</i>
     * @param graph A <code>Map</code> of <code>Town</code>s. Links among nodes are retrieved by calling
     * {@link Town#nearbiesIterator()} on a town
     * @param source The source Town of <code>graph</code> starting the research from
     * @param player <code>Player</code> which to check emporiums for
     * @return A List of towns reachable from <code>source</code> having an emporium of <code>player</code>
     */
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

    /**
     * A shortest-path tree generation algorithm from the input <code>graph</code>. Given input arguments, it builds a
     * Map of towns reachable from <code>source</code> with the cost to reach them. Each link on the graph has the
     * same cost of 2 (two) coins. Let <i>t</i> be a Town; the property of being reachable is such that <code>(* there
     * exists a path from source to t *) &amp;&amp; (* the sum of links' costs from source to t &lt;= coins *)</code>.
     * The algorithm is implemented with a variation of BFS algorithm from <i>Introduction to Algorithms by Thomas H.
     * Cormen, Charles E. Leiserson, Ronald L. Rivest e Clifford Stein</i>
     * @param graph A <code>Map</code> of <code>Town</code>s. Links among nodes are retrieved by calling
     * {@link Town#nearbiesIterator()} on a town
     * @param source The source Town of <code>graph</code> starting the research from
     * @param coins Maximum cost bound
     * @return A Map having as keys the <code>TownName</code>s of reachable towns and as values the costs to reach them
     */
    public static Map<TownName, Integer> reachableTowns(Map<TownName, TownInterface> graph, TownName source, int coins) {
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
