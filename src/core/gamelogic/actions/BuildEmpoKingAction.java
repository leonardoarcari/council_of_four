package core.gamelogic.actions;

import core.gamemodel.PoliticsCard;
import core.gamemodel.Town;
import core.Player;

import java.util.List;

/**
 * Created by Matteo on 23/05/16.
 */
public class BuildEmpoKingAction extends Action implements NormalAction {
    List<PoliticsCard> kingSatisfCards;
    Town buildingTown;

    public BuildEmpoKingAction(Player player, List<PoliticsCard> kingSatisfCards, Town buildingTown) {
        super(player);
        this.kingSatisfCards = kingSatisfCards;
        this.buildingTown = buildingTown;
    }

    public List<PoliticsCard> getKingSatisfCards() {
        return kingSatisfCards;
    }

    public Town getBuildingTown() {
        return buildingTown;
    }
}
