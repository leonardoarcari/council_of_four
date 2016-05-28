package core.gamelogic.actions;

import core.gamemodel.PermitCard;
import core.gamemodel.RegionType;
import core.gamemodel.Town;
import core.Player;
import core.gamemodel.TownName;

/**
 * Created by Matteo on 23/05/16.
 */
public class BuildEmpoPCAction extends Action implements NormalAction {
    TownName selectedTown;
    PermitCard usedPermitCard;
    RegionType regionType;

    public BuildEmpoPCAction(Player player,RegionType regionType, TownName selectedTown, PermitCard usedPermitCard) {
        super(player);
        this.regionType = regionType;
        this.selectedTown = selectedTown;
        this.usedPermitCard = usedPermitCard;
    }

    public RegionType getRegionType() {
        return regionType;
    }

    public TownName getSelectedTown() {
        return selectedTown;
    }

    public PermitCard getUsedPermitCard() {
        return usedPermitCard;
    }
}
