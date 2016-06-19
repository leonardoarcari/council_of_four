package core.gamemodel;

import core.Player;
import core.gamemodel.modelinterface.VictoryPathInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Matteo on 24/05/16.
 */
public class VictoryPath extends AbstractPath implements VictoryPathInterface {

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
