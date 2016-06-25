package core.gamemodel.modelinterface;

import core.gamemodel.*;
import javafx.scene.paint.Color;

import java.util.Iterator;

/**
 * This interface describes the methods the Player class. Moreover, it acts as
 * a proxy towards the client, forcing it to use only these methods. So this interface
 * is part of the Façade Pattern, applied to the model-client relationship.
 */
public interface PlayerInterface  {
    /**
     * @return the player's username
     */
    String getUsername();

    /**
     * @return the player's nickname
     */
    String getNickname();

    /**
     * @return the player's color, only used in the GUI interface
     */
    Color getColor();

    /**
     * @return the number of servants of the player
     */
    int getServantsNumber();

    /**
     * @return the number of permit cards of the player
     */
    int getPermitCardsNumber();

    /**
     * @return the number of politics cards of the player
     */
    int getPoliticsCardsNumber();

    /**
     * @return the number of royal cards of the player
     */
    int getRoyalCardsNumber();

    /**
     * @return the iterator on the permit cards of the player
     */
    Iterator<PermitCard> permitCardIterator();

    /**
     * @return the iterator on the royal cards of the player
     */
    Iterator<RoyalCard> royalCardIterator();

    /**
     * @return the iterator on the politics cards of the player
     */
    Iterator<PoliticsCard> politicsCardIterator();

    /**
     * @return the iterator on the region cards of the player
     */
    Iterator<RegionCard> regionCardIterator();

    /**
     * @return the iterator on the town type cards of the player
     */
    Iterator<TownTypeCard> townCardIterator();
}
