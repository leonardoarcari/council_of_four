package core.gamelogic.actions;

import core.Player;
import core.gamemodel.TownName;

/**
 * This class represents one of the special actions gained from
 * the nobility path; it allows the player to choose a bonus of
 * a town and take it.
 *
 * @see Action
 * @see SpecialAction
 */
public class PickTownBonusAction extends Action implements SpecialAction {
    // Name of the town that contains the bonus to pick
    private TownName townName;

    /**
     * @param player is the player doing the action
     * @param townName is the name of the town containing the bonus taken by the player
     */
    public PickTownBonusAction(Player player, TownName townName) {
        super(player);
        this.townName = townName;
    }

    /**
     * @return the town name
     */
    public TownName getTownName() {
        return townName;
    }
}
