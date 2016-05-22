package Core.GameModel.ModelInterface;

import Core.GameModel.PermitCard;
import Core.GameModel.RegionCard;

/**
 * Created by Leonardo Arcari on 22/05/2016.
 */
public interface RegionInterface {
    boolean isRegionCardTaken();
    PermitCard getLeftPermitCard();
    PermitCard getRightPermitCard();
    RegionCard getRegionCard();
}
