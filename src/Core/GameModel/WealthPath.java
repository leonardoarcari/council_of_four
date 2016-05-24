package Core.GameModel;

import Core.Player;

import java.util.ArrayList;

/**
 * Created by Matteo on 23/05/16.
 */
public class WealthPath extends AbstractPath{

    public WealthPath() {
        players = new ArrayList<>(21);
        players.stream().forEach(playerList -> playerList = new ArrayList<>());
    }

    public void setPlayer(Player player, int position) {
        players.get(position).add(player);
    }
}
