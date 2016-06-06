package core.gamemodel.modelinterface;

import core.gamemodel.PermitCard;
import core.gamemodel.RegionCard;
import core.gamemodel.RegionType;

/**
 * Created by Leonardo Arcari on 22/05/2016.
 */
public interface RegionInterface {
    boolean isRegionCardTaken();
    PermitCard getLeftPermitCard();
    PermitCard getRightPermitCard();
    RegionCard getRegionCard();
    RegionType getRegionType();
}
