package core.gamemodel;

import core.Player;
import core.gamemodel.bonus.Bonus;
import core.gamemodel.modelinterface.NobilityPathInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Created by Matteo on 23/05/16.
 */
public class NobilityPath extends AbstractPath implements NobilityPathInterface{
    private List<List<Bonus>> bonusPath;

    public List<List<Bonus>> getBonusPath() {
        return bonusPath;
    }

    public NobilityPath(List<List<Bonus>> bonusPath) {
        this.bonusPath = bonusPath;
        players = new ArrayList<>(21);
        for (int i = 0; i < 21; i++) {
            players.add(new ArrayList<>());
        }
    }

    public void setPlayer(Player player) {
        players.get(0).add(player);
    }

    public List<Bonus> retrieveBonus(Player player) {
        for (List<Player> al : players) {
            if (al.contains(player)) {
                int pathPosition = players.indexOf(al);
                return bonusPath.get(pathPosition);
            }
        }
        throw new NoSuchElementException();
    }
}
