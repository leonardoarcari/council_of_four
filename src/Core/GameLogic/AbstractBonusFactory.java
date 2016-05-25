package core.gamelogic;

import core.gamemodel.bonus.Bonus;

import java.util.List;

/**
 * Created by Leonardo Arcari on 25/05/2016.
 */
public interface AbstractBonusFactory {
    List<Bonus> generateBonuses();
}
