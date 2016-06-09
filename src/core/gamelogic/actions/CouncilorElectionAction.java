package core.gamelogic.actions;

import core.Player;
import core.gamemodel.Councilor;
import core.gamemodel.RegionType;

import java.io.Serializable;

/**
 * Created by Matteo on 23/05/16.
 */
public class CouncilorElectionAction extends Action implements NormalAction {
    private Councilor newCouncilor;
    private RegionType regionType;

    public CouncilorElectionAction(Player player, Councilor newCouncilor, RegionType regionType) {
        super(player);
        this.newCouncilor = newCouncilor;
        this.regionType = regionType;
    }

    public Councilor getNewCouncilor() {
        return newCouncilor;
    }

    public RegionType getRegionType() {
        return regionType;
    }
}
