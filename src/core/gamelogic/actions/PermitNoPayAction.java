package core.gamelogic.actions;

import core.Player;
import core.gamemodel.Region;
import core.gamemodel.RegionType;

/**
 * Created by Matteo on 28/05/16.
 */
public class PermitNoPayAction extends Action implements SpecialAction {
    private RegionType regionType;
    private Region.PermitPos position;

    public PermitNoPayAction(Player player, RegionType regionType, Region.PermitPos position) {
        super(player);
        this.regionType = regionType;
        this.position = position;
    }

    public RegionType getRegionType() {
        return regionType;
    }

    public Region.PermitPos getPosition() {
        return position;
    }
}
