package test;

import core.Player;
import core.gamemodel.VictoryPath;
import core.gamemodel.WealthPath;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.NoSuchElementException;

import static org.junit.Assert.*;

/**
 * Created by Matteo on 25/05/16.
 */
@RunWith(value = Parameterized.class)
public class WealthPathTest {
    private WealthPath wp00;
    private Player pl0;
    private WealthPath wp01;

    //Parameters for testing
    private int start = 0;
    private int increment;
    private int expectedPos;

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {-5, 0},
                {-1, 0},
                {1, 1},
                {12, 12},
                {20, 20},
                {35, 20}
        });
    }

    public WealthPathTest(int increment, int expectedPos) {
        this.increment = increment;
        this.expectedPos = expectedPos;
    }

    @Before
    public void setUp() {
        wp00 = new WealthPath();
        pl0 = new Player(null);
        wp01 = new WealthPath();
        wp01.setPlayer(pl0,start);
    }

    @Test(expected = NoSuchElementException.class)
    public void moveNoPlayer() {
        wp00.movePlayer(pl0,increment);
    }

    @Test public void movePlayer() {
        wp01.movePlayer(pl0,increment);
        assertEquals(wp01.getPlayerPosition(pl0),expectedPos);
    }

    @Test (expected = IllegalArgumentException.class)
    public void moveNullPlayer() {
        wp01.movePlayer(null,increment);
    }
}