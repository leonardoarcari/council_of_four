package core.gamelogic.actions;

import core.Player;
import core.gamemodel.Region;
import core.gamemodel.RegionType;

/**
 * This class represents one of the special actions gained from
 * the nobility path; it allows the player to choose a permit card
 * for free.
 *
 * @see Action
 * @see SpecialAction
 */
public class PermitNoPayAction extends Action implements SpecialAction {
    // Attributes of the action
    private RegionType regionType;
    private Region.PermitPos position;

    /**
     * @param player is the player doing the action
     * @param regionType is the type of the region that contains the permit card
     * @param position is the position of the card to take
     */
    public PermitNoPayAction(Player player, RegionType regionType, Region.PermitPos position) {
        super(player);
        this.regionType = regionType;
        this.position = position;
    }

    /**
     * @return the type of the region
     */
    public RegionType getRegionType() {
        return regionType;
    }

    /**
     * @return the position of the card
     */
    public Region.PermitPos getPosition() {
        return position;
    }
}
