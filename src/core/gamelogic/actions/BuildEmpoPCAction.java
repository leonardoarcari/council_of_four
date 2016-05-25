package core.gamelogic.actions;

import core.gamemodel.PermitCard;
import core.gamemodel.Town;
import core.Player;
import core.gamemodel.TownName;

/**
 * Created by Matteo on 23/05/16.
 */
public class BuildEmpoPCAction extends Action implements NormalAction {
    TownName selectedTown;
    PermitCard usedPermitCard;

    public BuildEmpoPCAction(Player player, TownName selectedTown, PermitCard usedPermitCard) {
        super(player);
        this.selectedTown = selectedTown;
        this.usedPermitCard = usedPermitCard;
    }

    public TownName getSelectedTown() {
        return selectedTown;
    }

    public PermitCard getUsedPermitCard() {
        return usedPermitCard;
    }
}
