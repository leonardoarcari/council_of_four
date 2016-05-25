package core.gamemodel;

import core.Player;

import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * Created by Matteo on 23/05/16.
 */
public class WealthPath extends AbstractPath{

    public WealthPath() {
        players = new ArrayList<>(21);
        for (int i = 0; i < 21; i++) {
            players.add(new ArrayList<>());
        }
    }

    @Override
    public void movePlayer(Player player, int variation) {
        if (player == null) throw new IllegalArgumentException();
        for (int i = 0; i < players.size(); i++) {
            if(players.get(i).contains(player)) {
                players.get(i).remove(player);
                int newPos = (i+variation < 20) ? ((i+variation >= 0) ? i+variation : 0) : 20;
                players.get(newPos).add(player);
                notifyObservers();
                return;
            }
        }
        throw new NoSuchElementException();

    }

    public void setPlayer(Player player, int position) {
        players.get(position).add(player);
    }
}
