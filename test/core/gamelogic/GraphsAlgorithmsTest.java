package core.gamelogic;

import core.Player;
import core.gamemodel.Town;
import core.gamemodel.TownName;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.*;

import static org.junit.Assert.*;

/**
 * Created by Matteo on 25/05/16.
 */
@RunWith(value = Parameterized.class)
public class GraphsAlgorithmsTest {
    private Player player;
    private List<Town> myTowns;
    private TownName source;
    private Town a00,b00,c00;
    private Town a01,b01,c01,d01,e01,f01;
    private Town a10,b10,c10;
    private Map<TownName, Town> graphNoConn;
    private Map<TownName, Town> graphMid;
    private Map<TownName, Town> graphAllConn;

    // Parameters for testing
    private int coins;
    private Map<TownName, Integer> expectedGraph;

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {0, new HashMap<TownName, Integer>(){{put(TownName.A, 0);}} },
                {2, new HashMap<TownName, Integer>() {{ put(TownName.A, 0);
                                                        put(TownName.B, 2);
                                                        put(TownName.C, 2);}}},
                {6, new HashMap<TownName, Integer>() {{ put(TownName.A, 0);
                                                        put(TownName.B, 2);
                                                        put(TownName.C, 2);
                                                        put(TownName.D, 4);
                                                        put(TownName.F, 4);
                                                        put(TownName.E, 6);}}}
        });
    }

    public GraphsAlgorithmsTest(int coins, Map<TownName, Integer> expectedGraph) {
        this.expectedGraph = expectedGraph;
        this.coins = coins;
    }

    @Before
    public void setUp() throws Exception {
        myTowns = new ArrayList<>();
        player = new Player(null);
        source = TownName.A;
        setUpNoConn();
        setUpMid();
        setUpAllConn();
    }

    private void setUpNoConn() {
        a00 = new Town(TownName.A,null);
        b00 = new Town(TownName.B, null);
        b00.setNearbyTowns(Arrays.asList(TownName.C));
        c00 = new Town(TownName.C, null);
        c00.setNearbyTowns(Arrays.asList(TownName.B));

        graphNoConn = new HashMap<>();
        graphNoConn.put(TownName.A,a00);
        graphNoConn.put(TownName.B,b00);
        graphNoConn.put(TownName.C,c00);
    }

    private void setUpMid() {
        a01 = new Town(TownName.A,null);
        a01.setNearbyTowns(Arrays.asList(TownName.B, TownName.C));
        b01 = new Town(TownName.B,null);
        b01.setNearbyTowns(Arrays.asList(TownName.A,TownName.C,TownName.D));
        c01 = new Town(TownName.C,null);
        c01.setNearbyTowns(Arrays.asList(TownName.A,TownName.B,TownName.F));
        c01.createEmporium(player);
        d01 = new Town(TownName.D,null);
        d01.setNearbyTowns(Arrays.asList(TownName.B,TownName.F,TownName.E));
        d01.createEmporium(player);
        e01 = new Town(TownName.E,null);
        e01.setNearbyTowns(Arrays.asList(TownName.D));
        e01.createEmporium(player);
        f01 = new Town(TownName.F,null);
        f01.setNearbyTowns(Arrays.asList(TownName.C,TownName.F));

        graphMid = new HashMap<>();
        graphMid.put(TownName.A,a01);
        graphMid.put(TownName.B,b01);
        graphMid.put(TownName.C,c01);
        graphMid.put(TownName.D,d01);
        graphMid.put(TownName.E,e01);
        graphMid.put(TownName.F,f01);
    }

    private void setUpAllConn() {
        a10 = new Town(TownName.A,null);
        a10.setNearbyTowns(Arrays.asList(TownName.B,TownName.C));
        b10 = new Town(TownName.B,null);
        b10.createEmporium(player);
        b10.setNearbyTowns(Arrays.asList(TownName.A,TownName.C));
        c10 = new Town(TownName.C,null);
        c10.setNearbyTowns(Arrays.asList(TownName.A,TownName.B));
        c10.createEmporium(player);

        graphAllConn = new HashMap<>();
        graphAllConn.put(TownName.A,a10);
        graphAllConn.put(TownName.B,b10);
        graphAllConn.put(TownName.C,c10);
    }

    @Test
    public void testNoConnected() {
        myTowns = GraphsAlgorithms.townsWithEmporiumOf(graphNoConn, source, player);
        assertTrue(myTowns.isEmpty());
    }

    @Test
    public void testSomeConnected() {
        myTowns = GraphsAlgorithms.townsWithEmporiumOf(graphMid, source, player);
        List<Town> expected = new ArrayList<>(Arrays.asList(c01));
        assertTrue(expected.containsAll(myTowns) && myTowns.containsAll(expected));
    }

    @Test
    public void testAllConnected() {
        myTowns = GraphsAlgorithms.townsWithEmporiumOf(graphAllConn, source, player);
        List<Town> expected = new ArrayList<>(Arrays.asList(b10,c10));
        assertTrue(expected.containsAll(myTowns) && myTowns.containsAll(expected));
    }

    @Test (expected = IllegalArgumentException.class)
    public void testNullParameters() {
        myTowns = GraphsAlgorithms.townsWithEmporiumOf(null, null, player);
    }

    @Test
    public void testMoveKingGraph() throws Exception {
        assertTrue(GraphsAlgorithms.reachableTowns(graphMid, TownName.A, coins).equals(expectedGraph));
    }
}