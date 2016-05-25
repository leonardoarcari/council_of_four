package test;

import core.Player;
import core.gamelogic.AbstractBonusFactory;
import core.gamelogic.BonusFactory;
import core.gamelogic.BonusOwner;
import core.gamemodel.NobilityPath;
import core.gamemodel.bonus.Bonus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.junit.Assert.*;

import java.util.*;

/**
 * Created by Leonardo Arcari on 25/05/2016.
 */
@RunWith(value = Parameterized.class)
public class NobilityPathTest {
    private NobilityPath pathWithPlayer;
    private NobilityPath pathWithoutPlayer;
    private Player player;

    //Parameters for testing
    private int incrementValue;
    private int expectedPosition;

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {5, 5},
                {10, 10},
                {20, 20},
                {25, 20}
        });
    }

    public NobilityPathTest(int incrementValue, int expectedPosition) {
        this.incrementValue = incrementValue;
        this.expectedPosition = expectedPosition;
    }

    @Before
    public void setUp() throws Exception {
        AbstractBonusFactory bonusFactory = BonusFactory.getFactory(BonusOwner.NOBILITY);
        List<List<Bonus>> bonusPath = new ArrayList<>(21);
        bonusPath.stream().forEach(bonuses -> bonuses = bonusFactory.generateBonuses());
        pathWithoutPlayer = new NobilityPath(bonusPath);

        pathWithPlayer = new NobilityPath(bonusPath);
        player = new Player(null);
        pathWithPlayer.setPlayer(player);
    }

    @Test (expected = NoSuchElementException.class)
    public void moveWithoutPlayer() {
        pathWithoutPlayer.movePlayer(player, 5);
    }

    @Test
    public void moveWithPlayer() throws Exception {
        pathWithPlayer.movePlayer(player, incrementValue);
        assertEquals(expectedPosition, pathWithPlayer.getPlayerPosition(player));
    }

    @Test (expected = IllegalArgumentException.class)
    public void moveWithPlayerNegative() throws Exception {
        pathWithPlayer.movePlayer(player, -10);
    }
}