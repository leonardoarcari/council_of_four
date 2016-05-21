package Core.GameModel;

import Core.GameModel.Bonus.VictoryPoint;

/**
 * Created by Matteo on 19/05/16.
 */
public enum RoyalCard {
    FIRST(new VictoryPoint(25)),
    SECOND(new VictoryPoint(18)),
    THIRD(new VictoryPoint(12)),
    FOURTH(new VictoryPoint(7)),
    FIFTH(new VictoryPoint(3));

    private VictoryPoint victoryPoint;

    RoyalCard(VictoryPoint victoryPoint) {
        this.victoryPoint = victoryPoint;
    }

    public VictoryPoint getRoyalBonus() {
        return victoryPoint;
    }
}
