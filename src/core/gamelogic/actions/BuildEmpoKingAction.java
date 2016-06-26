package core.gamelogic.actions;

import core.Player;
import core.gamemodel.PoliticsCard;
import core.gamemodel.RegionType;
import core.gamemodel.TownName;

import java.util.Iterator;
import java.util.List;

/**
 * This action represents the main action that allows the player to
 * build an emporium with the king's help.
 *
 * @see Action
 * @see NormalAction
 */
public class BuildEmpoKingAction extends Action implements NormalAction {
    // Attributes of the action class
    private final List<PoliticsCard> kingSatisfCards;
    private final int spentCoins;
    private final TownName startingTown;
    private final TownName buildingTown;
    private final RegionType regionType;

    /**
     * @param player is the player doing the action
     * @param kingSatisfCards is a list of cards that satisfy the king's balcony
     * @param regionType is the region where the emporium will be built
     * @param startingTown is the name of the town where the king is before the action
     * @param buildingTown is the name of the town where the king is after the action
     * @param spentCoins is the amount of coins spent to do this action
     */
    public BuildEmpoKingAction(Player player, List<PoliticsCard> kingSatisfCards, RegionType regionType, TownName startingTown, TownName buildingTown, int spentCoins) {
        super(player);
        this.kingSatisfCards = kingSatisfCards;
        this.regionType = regionType;
        this.buildingTown = buildingTown;
        this.startingTown = startingTown;
        this.spentCoins = spentCoins;
    }

    /**
     * @return the iterator of the satisfying politics cards
     */
    public Iterator<PoliticsCard> getSatCardIterator() {
            return kingSatisfCards.iterator();
    }

    /**
     * @return the region type
     */
    public RegionType getRegionType() {
        return regionType;
    }

    /**
     * @return the name of the town where the king was before the action
     */
    public TownName getStartingTown() {
        return startingTown;
    }

    /**
     * @return the name of the town where the king is after the action
     */
    public TownName getBuildingTown() {
        return buildingTown;
    }

    /**
     * @return the amount of coins spent
     */
    public int getSpentCoins() {
        return spentCoins;
    }
}
