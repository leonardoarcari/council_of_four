package core.gamemodel;

import core.Player;
import core.gamemodel.modelinterface.WealthPathInterface;

import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * This class is the Victory path object of the game.
 * @see AbstractPath
 * @see WealthPathInterface
 */
public class WealthPath extends AbstractPath implements WealthPathInterface{

    public WealthPath() {
        players = new ArrayList<>(21);
        for (int i = 0; i < 21; i++) {
            players.add(new ArrayList<>());
        }
    }

    /**
     * The difference between this method and the abstract class one is
     * that in the wealth path players can move in both directions, so
     * variation can be negative.
     *
     * @param player is the player to move
     * @param variation tells how many position the player has to move
     * @see AbstractPath
     */
    @Override
    public void movePlayer(Player player, int variation) {
        if (player == null) throw new IllegalArgumentException();
        for (int i = 0; i < players.size(); i++) {
            for(Player play : getPlayers().get(i)) {
                if (play.equals(player)) {
                    players.get(i).remove(play);
                    int newPos = (i + variation < 20) ? ((i + variation >= 0) ? i + variation : 0) : 20;
                    players.get(newPos).add(play);
                    notifyObservers();
                    return;
                }
            }
        }
        throw new NoSuchElementException();

    }

    /**
     * This method is invoked when creating the game board: each player must
     * start from the initial position
     *
     * @param player is the player whose position has to be set
     * @param position is the initial position of the player, 10 for the first,
     *                 11 for the second and so on
     */
    public void setPlayer(Player player, int position) {
        players.get(position).add(player);
    }
}
