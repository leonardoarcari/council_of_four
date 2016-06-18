package core.gamelogic.actions;

import core.Player;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by Leonardo Arcari on 17/06/2016.
 */
public class PodiumAction implements Serializable {
    private final Map<Integer, List<Player>> podium;

    public PodiumAction(Map<Integer, List<Player>> podium) {
        this.podium = podium;
    }

    public Map<Integer, List<Player>> getPodium() {
        return podium;
    }

    public List<Player> getFirst() {
        return podium.get(1);
    }

    public List<Player> getSecond() {
        return podium.get(2);
    }
}
