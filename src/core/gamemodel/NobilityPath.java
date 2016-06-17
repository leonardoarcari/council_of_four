package core.gamemodel;

import core.Player;
import core.gamemodel.bonus.Bonus;
import core.gamemodel.modelinterface.NobilityPathInterface;

import java.util.*;

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

    @Override
    public Map<Integer, List<Player>> getPodium() {
        Map<Integer, List<Player>> resultMap = new HashMap<>(2);
        int firstPosition = 0;
        int secondPosition = -1;
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).size() > 0) {
                secondPosition = firstPosition;
                firstPosition = i;
            }
        }
        resultMap.put(1, players.get(firstPosition));
        resultMap.put(2, players.get(secondPosition));
        return resultMap;
    }
}
