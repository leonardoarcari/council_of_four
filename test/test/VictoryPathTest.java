package test;

import core.Player;
import core.connection.SocketConnection;
import core.gamemodel.VictoryPath;
import org.junit.Before;
import org.junit.Test;

import java.util.NoSuchElementException;

import static org.junit.Assert.*;

/**
 * Created by Matteo on 25/05/16.
 */
public class VictoryPathTest {
    private VictoryPath vp00;
    private Player pl0;
    private VictoryPath vp01;

    @Before public void setUp() {
        vp00 = new VictoryPath();
        pl0 = new Player(null);
        vp01 = new VictoryPath();
        vp01.setPlayer(pl0);
    }

    @Test (expected = NoSuchElementException.class)
    public void moveNoPlayer() {

    }

    @Test public void movePlayer() {

    }
}