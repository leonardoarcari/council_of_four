package core.gamemodel.modelinterface;

import core.Player;
import core.gamemodel.Town;
import core.gamemodel.TownName;
import core.gamemodel.TownType;
import core.gamemodel.bonus.Bonus;

import java.util.Iterator;

/**
 * Created by Leonardo Arcari on 22/05/2016.
 */
public interface TownInterface {
    boolean isKingHere();
    TownType getTownType();
    TownName getTownName();
    boolean hasEmporium(Player player);
    Iterator<Player> getPlayersEmporium();
    Iterator<TownName> nearbiesIterator();
    Bonus getTownBonus();
}
