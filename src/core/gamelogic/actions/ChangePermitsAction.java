package core.gamelogic.actions;

import core.Player;
import core.gamemodel.RegionType;

/**
 * Created by Leonardo Arcari on 23/05/2016.
 */
public class ChangePermitsAction extends Action implements FastAction {
    private final RegionType regionType;

    public ChangePermitsAction(Player player, RegionType regionType) {
        super(player);
        this.regionType = regionType;
    }

    public RegionType getRegionType() {
        return regionType;
    }
}
