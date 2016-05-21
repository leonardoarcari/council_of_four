package Core.GameModel;

import Core.GameModel.Bonus.VictoryPoint;

/**
 * Created by Matteo on 20/05/16.
 */
public enum RegionCard {
    SEA(new VictoryPoint(5)),
    HILLS(new VictoryPoint(5)),
    MOUNTAINS(new VictoryPoint(5));

    private VictoryPoint victoryPoint;

    RegionCard(VictoryPoint victoryPoint) {
        this.victoryPoint = victoryPoint;
    }

    public VictoryPoint getRegionBonus() {
        return victoryPoint;
    }
}
