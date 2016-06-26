package core.gamemodel.modelinterface;

import core.gamemodel.PermitCard;
import core.gamemodel.RegionCard;
import core.gamemodel.RegionType;

/**
 * This interface describes the getter and utility methods the Region class. Moreover,
 * it acts as a proxy towards the client, forcing it to use only these methods.
 *
 * @see core.gamemodel.Region
 */
public interface RegionInterface {
    /**
     * @return whether the region card has been taken or not
     */
    boolean isRegionCardTaken();

    /**
     * @return the left permit card of the region
     */
    PermitCard getLeftPermitCard();

    /**
     * @return the right permit card of the region
     */
    PermitCard getRightPermitCard();

    /**
     * @return the region card
     */
    RegionCard getRegionCard();

    /**
     * @see RegionType
     * @return the type of the region
     */
    RegionType getRegionType();
}
