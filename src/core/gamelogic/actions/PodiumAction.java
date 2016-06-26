package core.gamelogic.actions;

import core.Player;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * This class represents the action that informs each player of the last
 * phase of the game: the podium exposure.
 */
public class PodiumAction implements Serializable {
    // The podium of the game
    private final Map<Integer, List<Player>> podium;

    /**
     * @param podium is the podium of the game
     */
    public PodiumAction(Map<Integer, List<Player>> podium) {
        this.podium = podium;
    }

    /**
     * @return the players in the first place of the podium
     */
    public List<Player> getFirst() {
        return podium.get(1);
    }

    /**
     * @return the players in the second place of the podium
     */
    public List<Player> getSecond() {
        return podium.get(2);
    }
}
