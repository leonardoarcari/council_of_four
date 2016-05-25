package core.gamelogic.actions;

import core.gamemodel.PermitCard;
import core.gamemodel.Town;
import core.Player;

/**
 * Created by Matteo on 23/05/16.
 */
public class BuildEmpoPCAction extends Action implements NormalAction {
    Town selectedTown;
    PermitCard usedPermitCard;

    public BuildEmpoPCAction(Player player, Town selectedTown, PermitCard usedPermitCard) {
        super(player);
        this.selectedTown = selectedTown;
        this.usedPermitCard = usedPermitCard;
    }

    public Town getSelectedTown() {
        return selectedTown;
    }

    public PermitCard getUsedPermitCard() {
        return usedPermitCard;
    }
}
