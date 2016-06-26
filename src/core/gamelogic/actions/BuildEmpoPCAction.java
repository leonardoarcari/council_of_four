package core.gamelogic.actions;

import core.Player;
import core.gamemodel.PermitCard;
import core.gamemodel.RegionType;
import core.gamemodel.TownName;

/**
 * This class represents the main action that allows the player to
 * build an emporium using the town permits of one of his permit cards.
 *
 * @see Action
 * @see NormalAction
 */
public class BuildEmpoPCAction extends Action implements NormalAction {
    // Attributes of the action class
    private TownName selectedTown;
    private PermitCard usedPermitCard;
    private RegionType regionType;

    /**
     * @param player is the player doing the action
     * @param regionType is the type of the region where the player wants to build
     * @param selectedTown is the name of the town where the player wants to build an emporium
     * @param usedPermitCard is the permit card used to build the emporium
     */
    public BuildEmpoPCAction(Player player, RegionType regionType, TownName selectedTown, PermitCard usedPermitCard) {
        super(player);
        this.regionType = regionType;
        this.selectedTown = selectedTown;
        this.usedPermitCard = usedPermitCard;
    }

    /**
     * @return the region type
     */
    public RegionType getRegionType() {
        return regionType;
    }

    /**
     * @return the town where the player wants to build the emporium
     */
    public TownName getSelectedTown() {
        return selectedTown;
    }

    /**
     * @return the permit card used to build the emporium
     */
    public PermitCard getUsedPermitCard() {
        return usedPermitCard;
    }
}
