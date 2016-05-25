package core.gamelogic;

import core.Player;
import core.gamemodel.Town;
import core.gamemodel.TownName;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 * Created by Matteo on 25/05/16.
 */
public class GraphsAlgorithmsTest {
    Player player;
    List<Town> myTowns;
    TownName source;
    Town a,b,c,d,e,f;
    Map<TownName, Town> graph;

    @Before public void setUp() throws Exception {
        player = new Player(null);
        source = TownName.A;
        a = new Town(TownName.A,null);
        a.setNearbyTowns(Arrays.asList(TownName.B, TownName.C));
        b = new Town(TownName.B,null);
        b.setNearbyTowns(Arrays.asList(TownName.A,TownName.C,TownName.D));
        c = new Town(TownName.C,null);
        c.setNearbyTowns(Arrays.asList(TownName.A,TownName.B,TownName.F));
        c.createEmporium(player);
        d = new Town(TownName.D,null);
        d.setNearbyTowns(Arrays.asList(TownName.B,TownName.F,TownName.E));
        d.createEmporium(player);
        e = new Town(TownName.E,null);
        e.setNearbyTowns(Arrays.asList(TownName.D));
        e.createEmporium(player);
        f = new Town(TownName.F,null);
        f.setNearbyTowns(Arrays.asList(TownName.C,TownName.F));

        graph = new HashMap<>();
        graph.put(TownName.A,a);
        graph.put(TownName.B,b);
        graph.put(TownName.C,c);
        graph.put(TownName.D,d);
        graph.put(TownName.E,e);
        graph.put(TownName.F,f);
    }

    @Test public void testAlgorithm() {
        myTowns = GraphsAlgorithms.townsWithEmporiumOf(player, source, graph);
        List<Town> expected = new ArrayList<>(Arrays.asList(c,d,e));
        assertTrue(expected.containsAll(myTowns));
        assertTrue(myTowns.containsAll(expected));
    }
}