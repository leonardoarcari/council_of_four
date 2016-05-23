package Core.GameModel;

import Core.GameModel.Bonus.Bonus;
import Core.Player;
import Server.Observer;

import java.util.*;

/**
 * Created by Matteo on 23/05/16.
 */
public class NobilityPath extends AbstractPath{
    private ArrayList<ArrayList<Bonus>> bonusPath;

    public NobilityPath(List<Observer> observers) {
        super(observers);
        bonusPath = new ArrayList<>(21);
        bonusPath.stream().forEach(bonuses -> bonuses = new ArrayList<>());
        players = new ArrayList<>(21);
        players.stream().forEach(playerList -> playerList = new ArrayList<>());

        int bonusNumber = generateBonusNumber();
    }

    private int generateBonusNumber () {
        float generator = new Random().nextFloat();
        if(generator < 10f/21) {
            return 0;
        } else if(generator < 16f/21) {
            return 1;
        } else return 2;
    }

    public void setPlayer(Player player) {
        players.get(0).add(player);
    }

    public ArrayList<Bonus> retrieveBonus(Player player) {
        for (ArrayList<Player> al : players) {
            if(al.contains(player)) {
                int pathPosition = players.indexOf(al);
                return bonusPath.get(pathPosition);
            }
        }
        throw new NoSuchElementException();
    }
}
