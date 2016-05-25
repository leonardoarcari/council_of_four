package core.gamemodel;

import core.Player;

import java.util.ArrayList;

/**
 * Created by Matteo on 24/05/16.
 */
public class VictoryPath extends AbstractPath {

    public VictoryPath() {
        players = new ArrayList<>(100);
        for(int i = 0; i < 100; i++) {
            players.add(new ArrayList<>());
        }
    }

    public void setPlayer(Player player) {
        players.get(0).add(player);
    }
}
