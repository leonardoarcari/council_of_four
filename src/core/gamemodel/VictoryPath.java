package core.gamemodel;

import core.Player;
import core.gamemodel.modelinterface.VictoryPathInterface;

import java.util.ArrayList;
import java.util.NoSuchElementException;

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

    public void setPlayer(Player player) {
        players.get(0).add(player);
    }
}
