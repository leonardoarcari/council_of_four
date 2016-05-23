package Core.GameLogic.Actions;

import Core.GameModel.PermitCard;
import Core.GameModel.PoliticsCard;
import Core.GameModel.RegionType;
import Core.Player;

import java.util.List;

/**
 * Created by Matteo on 23/05/16.
 */
public class BuyPermitCardAction extends Action implements NormalAction{
    List<PoliticsCard> discardedCards;
    RegionType regionType;
    PermitCard drawnPermitCard;

    public BuyPermitCardAction(Player player, List<PoliticsCard> discardedCards, RegionType regionType, PermitCard drawnPermitCard) {
        super(player);
        this.discardedCards = discardedCards;
        this.regionType = regionType;
        this.drawnPermitCard = drawnPermitCard;
    }

    public List<PoliticsCard> getDiscardedCards() {
        return discardedCards;
    }

    public RegionType getRegionType() {
        return regionType;
    }

    public PermitCard getDrawnPermitCard() {
        return drawnPermitCard;
    }
}
