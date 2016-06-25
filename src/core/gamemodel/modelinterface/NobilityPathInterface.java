package core.gamemodel.modelinterface;

import core.gamemodel.bonus.Bonus;

import java.util.List;

/**
 * This interface acts as a specific marker of the NobilityPath class
 * @see AbstractPathInterface
 * @see core.gamemodel.NobilityPath
 */
public interface NobilityPathInterface extends AbstractPathInterface {
    /**
     * @return a list of bonuses for each position of the nobility path
     */
    List<List<Bonus>> getBonusPath();
}
