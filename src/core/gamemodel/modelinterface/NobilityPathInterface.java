package core.gamemodel.modelinterface;

import core.gamemodel.bonus.Bonus;

import java.util.List;

/**
 * Created by Matteo on 06/06/16.
 */
public interface NobilityPathInterface extends AbstractPathInterface {
    List<List<Bonus>> getBonusPath();
}
