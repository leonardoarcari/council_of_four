package Core.GameModel;

import Core.Player;
import Server.Observer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matteo on 23/05/16.
 */
public class WealthPath extends AbstractPath{

    public WealthPath(List<Observer> observers) {
        super(observers);
        players = new ArrayList<>(21);
        players.stream().forEach(playerList -> playerList = new ArrayList<>());
    }

    public void setPlayer(Player player, int position) {
        players.get(position).add(player);
    }
}
