package core.gamelogic.actions;

import core.gamemodel.RegionType;
import core.Player;

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
