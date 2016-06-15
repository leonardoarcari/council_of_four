package core.gamemodel;

import core.gamemodel.bonus.VictoryPoint;

import java.io.Serializable;

/**
 * Created by Matteo on 20/05/16.
 */
public enum TownTypeCard implements Serializable {
    GOLD(new VictoryPoint(20), TownType.GOLD),
    SILVER(new VictoryPoint(12), TownType.SILVER),
    BRONZE(new VictoryPoint(8), TownType.BRONZE),
    IRON(new VictoryPoint(5), TownType.IRON);

    private VictoryPoint victoryPoint;
    private TownType townType;

    TownTypeCard(VictoryPoint victoryPoint, TownType townType) {
        this.victoryPoint = victoryPoint;
        this.townType = townType;
    }

    public VictoryPoint getTypeBonus() {
        return victoryPoint;
    }

    public TownType getTownType() {
        return townType;
    }

    @Override
    public String toString() {
        String townCardString = townType.name().toLowerCase();
        townCardString = String.valueOf(Character.toUpperCase(townCardString.charAt(0))) + townCardString.substring(1);
        townCardString = townCardString + ", " + victoryPoint.toString();
        return townCardString;
    }
}
