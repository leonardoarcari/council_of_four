package Core.GameLogic.Actions;

import Core.GameModel.RegionType;
import Core.Player;

/**
 * Created by Leonardo Arcari on 23/05/2016.
 */
public class ChangePermitsAction extends Action implements FastAction {
    private final RegionType regionType;

    public ChangePermitsAction(Player player, RegionType regionType) {
        super();
        this.regionType = regionType;
    }

    public RegionType getRegionType() {
        return regionType;
    }
}
