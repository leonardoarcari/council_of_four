package core.gamemodel.modelinterface;

import core.gamemodel.bonus.Bonus;
import core.gamemodel.TownType;
import core.Player;

import java.util.Iterator;

/**
 * Created by Leonardo Arcari on 22/05/2016.
 */
public interface TownInterface {
    boolean isKingHere();
    TownType getTownType();
    boolean hasEmporium(Player player);
    Iterator<Bonus> bonusIterator();
}
