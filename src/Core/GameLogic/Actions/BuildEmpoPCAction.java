package Core.GameLogic.Actions;

import Core.GameModel.PermitCard;
import Core.GameModel.Town;
import Core.Player;

/**
 * Created by Matteo on 23/05/16.
 */
public class BuildEmpoPCAction extends Action implements NormalAction {
    Town selectedTown;
    PermitCard usedPermitCard;

    public BuildEmpoPCAction(Town selectedTown, PermitCard usedPermitCard) {
        super();
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
