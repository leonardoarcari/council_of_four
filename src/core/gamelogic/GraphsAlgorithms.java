package core.gamelogic;

import core.Player;
import core.gamemodel.GameBoard;
import core.gamemodel.Town;
import core.gamemodel.TownName;

import java.util.*;

/**
 * Created by Leonardo Arcari on 25/05/2016.
 */
public class GraphsAlgorithms {
    public static List<Town> townsWithEmporiumOf(Player player, TownName source, Map<TownName, Town> graph) {
        if(source == null || graph == null) throw new IllegalArgumentException();
        List<Town> returnList = new ArrayList<>();
        Map<TownName, Node> graphNodes = new HashMap<>(graph.size());
        for (Town t : graph.values()) {
            graphNodes.put(t.getTownName(), new Node(t, Color.WHITE, -1));
        }
        Node sourceNode = new Node(graph.get(source), Color.GRAY, 0);
        ArrayDeque<Node> deque = new ArrayDeque<>();
        deque.addFirst(sourceNode);
        while (!deque.isEmpty()) {
            Node node = deque.removeLast();
            Iterator<TownName> nearbiesIterator = node.town.nearbiesIterator();
            while (nearbiesIterator.hasNext()) {
                TownName nearbyName = nearbiesIterator.next();
                Node nearbyNode = graphNodes.get(nearbyName);
                if (nearbyNode.color.equals(Color.WHITE) && nearbyNode.town.hasEmporium(player)) {
                    nearbyNode.color = Color.GRAY;
                    nearbyNode.d = node.d + 1;
                    returnList.add(nearbyNode.town);
                    deque.addFirst(nearbyNode);
                }
            }
            node.color = Color.BLACK;
        }
        return returnList;
    }

    private static class Node {
        Town town;
        Color color;
        int d;

        public Node(Town town, Color color, int d) {
            this.town = town;
            this.color = color;
            this.d = d;
        }
    }

    private enum Color {
        WHITE, GRAY, BLACK;
    }
}
