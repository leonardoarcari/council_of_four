package core.gamelogic.actions;

import core.gamemodel.PoliticsCard;
import core.gamemodel.Region;
import core.gamemodel.RegionType;
import core.Player;

import java.util.Iterator;
import java.util.List;

/**
 * Created by Matteo on 23/05/16.
 */
public class BuyPermitCardAction extends Action implements NormalAction{
    private List<PoliticsCard> discardedCards;
    private RegionType regionType;
    private Region.PermitPos position;

    public BuyPermitCardAction(Player player, List<PoliticsCard> discardedCards, RegionType regionType, Region.PermitPos position) {
        super(player);
        this.discardedCards = discardedCards;
        this.regionType = regionType;
        this.position = position;
    }

    public Iterator<PoliticsCard> discartedIterator() {
        return discardedCards.iterator();
    }

    public RegionType getRegionType() {
        return regionType;
    }

    public Region.PermitPos getDrawnPermitCard() {
        return position;
    }
}
