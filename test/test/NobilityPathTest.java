package test;

import core.Player;
import core.gamelogic.AbstractBonusFactory;
import core.gamelogic.BonusFactory;
import core.gamelogic.BonusOwner;
import core.gamemodel.NobilityPath;
import core.gamemodel.bonus.Bonus;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.Assert.*;

/**
 * Created by Leonardo Arcari on 25/05/2016.
 */
public class NobilityPathTest {
    private NobilityPath pathWithPlayer;
    private NobilityPath pathWithoutPlayer;
    private Player player;

    @Before
    public void setUp() {
        AbstractBonusFactory bonusFactory = BonusFactory.getFactory(BonusOwner.NOBILITY);
        List<List<Bonus>> bonusPath = new ArrayList<>(21);
        bonusPath.stream().forEach(bonuses -> bonuses = bonusFactory.generateBonuses());
        pathWithoutPlayer = new NobilityPath(bonusPath);

        pathWithPlayer = new NobilityPath(bonusPath);
        player = new Player(null);
        pathWithPlayer.setPlayer(player);
    }

    @Test (expected = NoSuchElementException.class)
    public void testMoveWithoutPlayer() {
        pathWithoutPlayer.movePlayer(player, 5);
    }

}