package core.gamemodel.modelinterface;

import core.gamemodel.PermitCard;
import core.gamemodel.RegionCard;

/**
 * Created by Leonardo Arcari on 22/05/2016.
 */
public interface RegionInterface {
    boolean isRegionCardTaken();
    PermitCard getLeftPermitCard();
    PermitCard getRightPermitCard();
    RegionCard getRegionCard();
}
