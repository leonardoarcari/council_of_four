package Core.GameLogic.Actions;

import Core.GameModel.PoliticsCard;
import Core.GameModel.Region;
import Core.GameModel.RegionType;

import java.util.Iterator;
import java.util.List;

/**
 * Created by Matteo on 23/05/16.
 */
public class BuyPermitCardAction extends Action implements NormalAction{
    private List<PoliticsCard> discardedCards;
    private RegionType regionType;
    private Region.PermitPos position;

    public BuyPermitCardAction(List<PoliticsCard> discardedCards, RegionType regionType, Region.PermitPos drawnPermitCard) {
        super();
        this.discardedCards = discardedCards;
        this.regionType = regionType;
        this.position = drawnPermitCard;
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
