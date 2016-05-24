package Core.GameLogic.Actions;

import Core.GameModel.Councilor;
import Core.GameModel.RegionType;
import Core.Player;

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
