package core.gamelogic.actions;

import core.gamemodel.Councilor;
import core.gamemodel.RegionType;
import core.Player;

/**
 * Created by Leonardo Arcari on 23/05/2016.
 */
public class FastCouncilorElectionAction extends Action implements FastAction {
    private final RegionType regionType;
    private final Councilor councilor;

    public FastCouncilorElectionAction(Player player, RegionType regionType, Councilor councilor) {
        super(player);
        this.regionType = regionType;
        this.councilor = councilor;
    }

    public RegionType getRegionType() {
        return regionType;
    }

    public Councilor getCouncilor() {
        return councilor;
    }
}
