package core.gamemodel.modelinterface;

import core.Player;

import java.util.List;
import java.util.Map;

/**
 * This interface describes the methods of the three game paths classes. Moreover, it acts as
 * a proxy towards the client, forcing it to use only these methods. So this interface
 * is part of the Façade Pattern, applied to the model-client relationship.
 */
public interface AbstractPathInterface {
    /**
     * @return the number of players and the players of the relative path, for each position
     */
    List<List<Player>> getPlayers();

    /**
     * This method searches for the player in the path and returns his position
     *
     * @param player is the player asking for his position
     * @return the player position in the relative path
     */
    int getPlayerPosition(Player player);

    /**
     * This method creates the podium for the relative path, including the players
     * with most points (coins for the wealth path). It only creates first and second
     * places lists of players.
     *
     * @return the podium map
     */
    Map<Integer, List<Player>> getPodium();
}
