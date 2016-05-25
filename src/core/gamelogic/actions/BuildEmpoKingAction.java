package core.gamelogic.actions;

import core.gamemodel.PoliticsCard;
import core.gamemodel.Town;
import core.Player;
import core.gamemodel.TownName;

import java.util.Iterator;
import java.util.List;

/**
 * Created by Matteo on 23/05/16.
 */
public class BuildEmpoKingAction extends Action implements NormalAction {
    List<PoliticsCard> kingSatisfCards;
    int spentCoins;
    TownName startingTown;
    TownName buildingTown;

    public BuildEmpoKingAction(Player player, List<PoliticsCard> kingSatisfCards,TownName startingTown, TownName buildingTown, int spentCoins) {
        super(player);
        this.kingSatisfCards = kingSatisfCards;
        this.buildingTown = buildingTown;
        this.startingTown = startingTown;
        this.spentCoins = spentCoins;
    }

    public Iterator<PoliticsCard> getSatCardIterator() {
            return kingSatisfCards.iterator();
    }

    public TownName getStartingTown() {
        return startingTown;
    }

    public TownName getBuildingTown() {
        return buildingTown;
    }
}
