package core.gamelogic.actions;

import core.Player;
import core.gamemodel.Councilor;
import core.gamemodel.RegionType;

/**
 * This class represents the fast action that allows the player to
 * elect a councilor.
 *
 * @see Action
 * @see FastAction
 */
public class FastCouncilorElectionAction extends Action implements FastAction {
    // Attributes of the action
    private final RegionType regionType;
    private final Councilor councilor;

    /**
     * @param player is the player doing the action
     * @param councilor is the councilor to elect
     * @param regionType is the region that contains the balcony where the councilor has to be elected
     */
    public FastCouncilorElectionAction(Player player, RegionType regionType, Councilor councilor) {
        super(player);
        this.regionType = regionType;
        this.councilor = councilor;
    }

    /**
     * @return the region type
     */
    public RegionType getRegionType() {
        return regionType;
    }

    /**
     * @return the councilor
     */
    public Councilor getCouncilor() {
        return councilor;
    }
}
