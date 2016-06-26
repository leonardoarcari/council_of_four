package core.gamemodel.modelinterface;

import core.gamemodel.Councilor;
import core.gamemodel.RegionType;

import java.util.Iterator;

/**
 * This interface describes the getter methods the Balcony class. Moreover, it acts as
 * a proxy towards the client, forcing it to use only these methods.
 *
 * @see core.gamemodel.CouncilorsBalcony
 */
public interface BalconyInterface {
    /**
     * @return the iterator of the balcony councilors
     */
    Iterator<Councilor> councilorsIterator();

    /**
     * @return the region type in which the balcony is
     */
    RegionType getRegion();

    /**
     * @return a string describing the balcony status
     */
    String toFormattedString();
}
