package core.gamelogic.actions;

import core.Player;
import core.gamemodel.PoliticsCard;
import core.gamemodel.RegionType;
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
    RegionType regionType;

    public BuildEmpoKingAction(Player player, List<PoliticsCard> kingSatisfCards, RegionType regionType, TownName startingTown, TownName buildingTown, int spentCoins) {
        super(player);
        this.kingSatisfCards = kingSatisfCards;
        this.regionType = regionType;
        this.buildingTown = buildingTown;
        this.startingTown = startingTown;
        this.spentCoins = spentCoins;
    }

    public Iterator<PoliticsCard> getSatCardIterator() {
            return kingSatisfCards.iterator();
    }

    public RegionType getRegionType() {
        return regionType;
    }

    public TownName getStartingTown() {
        return startingTown;
    }

    public TownName getBuildingTown() {
        return buildingTown;
    }

    public int getSpentCoins() {
        return spentCoins;
    }
}
