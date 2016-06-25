package core.gamemodel.modelinterface;

import core.gamemodel.Councilor;
import core.gamemodel.RoyalCard;
import core.gamemodel.TownTypeCard;

import java.util.Iterator;

/**
 * This interface describes the methods of the game board class. Moreover, it acts as
 * a proxy towards the client, forcing it to use only these methods. So this interface
 * is part of the Façade Pattern, applied to the model-client relationship.
 * @see core.gamemodel.GameBoard
 */
public interface GameBoardInterface {
    /**
     * @return the iterator of the royal cards
     */
    Iterator<RoyalCard> royalCardIterator();

    /**
     * @return the iterator of the councilor of the game board balcony
     */
    Iterator<Councilor> councilorIterator();

    /**
     * @return the iterator of the town type cards
     */
    Iterator<TownTypeCard> townTypeCardIterator();
}
