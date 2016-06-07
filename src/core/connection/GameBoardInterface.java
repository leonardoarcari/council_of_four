package core.connection;

import core.gamemodel.Councilor;
import core.gamemodel.RoyalCard;
import core.gamemodel.TownTypeCard;

import java.util.Iterator;

/**
 * Created by Matteo on 07/06/16.
 */
public interface GameBoardInterface {
    Iterator<RoyalCard> royalCardIterator();
    Iterator<Councilor> councilorIterator();
    Iterator<TownTypeCard> townTypeCardIterator();
}
