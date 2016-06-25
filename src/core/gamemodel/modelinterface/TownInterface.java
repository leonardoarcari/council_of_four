package core.gamemodel.modelinterface;

import core.Player;
import core.gamemodel.TownName;
import core.gamemodel.TownType;
import core.gamemodel.bonus.Bonus;

import java.util.Iterator;

/**
 * This interface contains the methods of the Town class. Moreover, it acts as
 * a proxy towards the client, forcing it to use only these methods. So this interface
 * is part of the Façade Pattern, applied to the model-client relationship.
 */
public interface TownInterface {
    /**
     * @return whether the king is in this town or not
     */
    boolean isKingHere();

    /**
     * @see TownType
     * @return the type of the town
     */
    TownType getTownType();

    /**
     * @see TownName
     * @return the name of the town
     */
    TownName getTownName();

    /**
     * @param player is one of the player of the game
     * @return whether player has already built an emporium in this town
     */
    boolean hasEmporium(Player player);

    /**
     * @return the iterator of players who have already built an emporium in this town
     */
    Iterator<Player> getPlayersEmporium();

    /**
     * @return the number of emporiums in this town
     */
    int getEmporiumsNumber();

    /**
     * @return an iterator of the names of the connected towns
     */
    Iterator<TownName> nearbiesIterator();

    /**
     * @return the bonus of this town
     */
    Bonus getTownBonus();
}
