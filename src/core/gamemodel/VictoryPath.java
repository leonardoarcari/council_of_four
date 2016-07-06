package core.gamemodel;

import core.Player;
import core.gamemodel.modelinterface.VictoryPathInterface;

import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * This class is the Victory path object of the game.
 * @see AbstractPath
 * @see VictoryPathInterface
 */
public class VictoryPath extends AbstractPath implements VictoryPathInterface {

    public VictoryPath() {
        players = new ArrayList<>(100);
        for(int i = 0; i < 100; i++) {
            players.add(new ArrayList<>());
        }
    }

    /**
     * The difference between this method and the abstract class one is
     * that the maximum value is 100, not 20.
     *
     * @param player is the player to move
     * @param variation tells how many position the player has to move
     * @see AbstractPath
     */
    @Override
    public void movePlayer(Player player, int variation) {
        if (variation <= 0 || player == null) {
            throw new IllegalArgumentException();
        }
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).contains(player)) {
                players.get(i).remove(player);
                int newPos = (i+variation < 100) ? i+variation : 100;
                players.get(newPos).add(player);
                notifyObservers();
                return;
            }
        }
        throw new NoSuchElementException();
    }

    /**
     * This method is invoked when creating the game board: each player must
     * start from the initial position of zero
     *
     * @param player is the player whose position has to be set
     */
    public void setPlayer(Player player) {
        players.get(0).add(player);
    }
}
