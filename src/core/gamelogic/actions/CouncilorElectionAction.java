package core.gamelogic.actions;

import core.Player;
import core.gamemodel.Councilor;
import core.gamemodel.RegionType;

/**
 * This class represents the main action that allows the player to
 * elect a councilor.
 *
 * @see Action
 * @see NormalAction
 */
public class CouncilorElectionAction extends Action implements NormalAction {
    // Attributes of the action
    private Councilor newCouncilor;
    private RegionType regionType;

    /**
     * @param player is the player doing the action
     * @param newCouncilor is the councilor to elect
     * @param regionType is the region that contains the balcony where the councilor has to be elected
     */
    public CouncilorElectionAction(Player player, Councilor newCouncilor, RegionType regionType) {
        super(player);
        this.newCouncilor = newCouncilor;
        this.regionType = regionType;
    }

    /**
     * @return the councilor
     */
    public Councilor getNewCouncilor() {
        return newCouncilor;
    }

    /**
     * @return the region type
     */
    public RegionType getRegionType() {
        return regionType;
    }
}
