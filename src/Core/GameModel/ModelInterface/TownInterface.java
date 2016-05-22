package Core.GameModel.ModelInterface;

import Core.GameModel.Bonus.Bonus;
import Core.GameModel.TownType;
import Core.Player;

/**
 * Created by Leonardo Arcari on 22/05/2016.
 */
public interface TownInterface {
    boolean isKingHere();
    TownType getTownType();
    boolean hasEmporium(Player player);
    Bonus getTownBonus();
}
