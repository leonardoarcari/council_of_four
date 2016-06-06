package core.gamemodel.modelinterface;

import core.gamemodel.Councilor;
import core.gamemodel.RegionType;

import java.util.Iterator;

/**
 * Created by Leonardo Arcari on 22/05/2016.
 */
public interface BalconyInterface {
    Iterator<Councilor> councilorsIterator();
    RegionType getRegion();
}
