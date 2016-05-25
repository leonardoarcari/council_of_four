package core.gamemodel;

import core.Player;
import core.gamemodel.VictoryPath;
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
public class VictoryPathTest {
    private VictoryPath vp00;
    private Player pl0;
    private VictoryPath vp01;

    //Parameters for testing
    private int increment;
    private int expectedPos;

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {2, 2},
                {7, 7},
                {20, 20},
                {105, 100}
        });
    }

    public VictoryPathTest(int increment, int expectedPos) {
        this.increment = increment;
        this.expectedPos = expectedPos;
    }

    @Before public void setUp() {
        vp00 = new VictoryPath();
        pl0 = new Player(null);
        vp01 = new VictoryPath();
        vp01.setPlayer(pl0);
    }

    @Test (expected = NoSuchElementException.class)
    public void moveNoPlayer() {
        vp00.movePlayer(pl0,increment);
    }

    @Test public void movePlayer() {
        vp01.movePlayer(pl0,increment);
        assertEquals(vp01.getPlayerPosition(pl0),expectedPos);
    }

    @Test (expected = IllegalArgumentException.class)
    public void moveNegPlayer() {
        vp01.movePlayer(pl0,-increment);
    }

    @Test (expected = IllegalArgumentException.class)
    public void moveNullPlayer() {
        vp01.movePlayer(null,increment);
    }
}