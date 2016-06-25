package core.gamemodel;

import core.Player;
import core.gamemodel.bonus.Bonus;
import core.gamemodel.modelinterface.NobilityPathInterface;

import java.util.*;

/**
 * This class is the Nobility path object of the game.
 * @see AbstractPath
 * @see NobilityPathInterface
 */
public class NobilityPath extends AbstractPath implements NobilityPathInterface{
    // Attributes of the class
    private List<List<Bonus>> bonusPath;
    public List<List<Bonus>> getBonusPath() {
        return bonusPath;
    }

    /**
     * The constructor initialize the players list and sets the bonuses for each
     * position of the path.
     *
     * @param bonusPath are the list of bonuses to insert into the nobility path
     */
    public NobilityPath(List<List<Bonus>> bonusPath) {
        this.bonusPath = bonusPath;
        players = new ArrayList<>(21);
        for (int i = 0; i < 21; i++) {
            players.add(new ArrayList<>());
        }
    }

    /**
     * This method is invoked when creating the game board: each player must
     * start from the initial position of zero
     *
     * @param player is the player whose position has to be set
     */
    public void setPlayer(Player player) {
        players.get(0).add(player);
    }

    /**
     * This method is invoked when a player moves on the path and he has to
     * redeem the bonus gained during the shift
     *
     * @param player is the player that moved on the path
     * @param positionBefore is the position of the player before he moved
     * @return the list of the bonus gained by the player moving on the nobility path
     */
    public List<Bonus> retrieveBonus(Player player, int positionBefore) {
        List<Bonus> bonusesToRetrieve = new ArrayList<>();

        if(positionBefore != 20) {// The player can't gain the last bonus every time he moves
            for (int i = positionBefore; i < getPlayerPosition(player); i++)
                bonusesToRetrieve.addAll(getBonusPath().get(i));

            players.stream().filter(al -> al.contains(player)).forEach(al -> {
                int pathPosition = players.indexOf(al);
                bonusesToRetrieve.addAll(bonusPath.get(pathPosition));
            });
        }
        return bonusesToRetrieve;
    }
}
