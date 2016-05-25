package core.gamemodel;

import core.Player;

import java.util.ArrayList;

/**
 * Created by Matteo on 24/05/16.
 */
public class VictoryPath extends AbstractPath {

    public VictoryPath() {
        players = new ArrayList<>(21);
        players.stream().forEach(playerList -> playerList = new ArrayList<>());
    }

    public void setPlayer(Player player) {
        players.get(0).add(player);
    }
}
