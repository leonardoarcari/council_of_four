package core.gamelogic.actions;

import core.Player;
import core.gamemodel.RegionType;

/**
 * This class represents the fast action that allow the player to
 * change the currently exposed permit cards of a region, popping
 * the first two of the deck and putting the discarded on its bottom.
 *
 * @see Action
 * @see FastAction
 */
public class ChangePermitsAction extends Action implements FastAction {
    // Attribute of the action
    private final RegionType regionType;

    /**
     * @param player is the player doing the action
     * @param regionType is the type of the region whose permit cards are being changed
     */
    public ChangePermitsAction(Player player, RegionType regionType) {
        super(player);
        this.regionType = regionType;
    }

    /**
     * @return the type of the region
     */
    public RegionType getRegionType() {
        return regionType;
    }
}
