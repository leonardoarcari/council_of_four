package core.gamelogic.actions;

import core.Player;
import core.gamemodel.PoliticsCard;
import core.gamemodel.Region;
import core.gamemodel.RegionType;

import java.util.Iterator;
import java.util.List;

/**
 * This class represents the normal action that allows the player to
 * buy a permit card, after having satisfied a balcony.
 */
public class BuyPermitCardAction extends Action implements NormalAction{
    // Attributes of the action
    private List<PoliticsCard> discardedCards;
    private RegionType regionType;
    private Region.PermitPos position;

    /**
     * @param player is the player doing the action
     * @param discardedCards is the list of the card used to satisfy a balcony
     * @param regionType is the region whose balcony is being satisfied
     * @param position is the position of the permit card to obtain
     */
    public BuyPermitCardAction(Player player, List<PoliticsCard> discardedCards, RegionType regionType, Region.PermitPos position) {
        super(player);
        this.discardedCards = discardedCards;
        this.regionType = regionType;
        this.position = position;
    }

    /**
     * @return the iterator on the discarded cards
     */
    public Iterator<PoliticsCard> discartedIterator() {
        return discardedCards.iterator();
    }

    /**
     * @return the type of the region that contains the wanted permit card
     */
    public RegionType getRegionType() {
        return regionType;
    }

    /**
     * @return the position of the permit card inside the region
     */
    public Region.PermitPos getDrawnPermitCard() {
        return position;
    }
}
