package core.gamemodel.modelinterface;

import core.Player;
import core.gamemodel.bonus.Bonus;

import java.util.List;
import java.util.Map;

/**
 * Created by Matteo on 06/06/16.
 */
public interface NobilityPathInterface extends AbstractPathInterface {
    List<List<Bonus>> getBonusPath();
}
