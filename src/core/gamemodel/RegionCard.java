package core.gamemodel;

import core.gamemodel.bonus.VictoryPoint;

import java.io.Serializable;

/**
 * Created by Matteo on 20/05/16.
 */
public enum RegionCard implements Serializable {
    SEA(new VictoryPoint(5), RegionType.SEA),
    HILLS(new VictoryPoint(5), RegionType.HILLS),
    MOUNTAINS(new VictoryPoint(5), RegionType.MOUNTAINS),
    NULL(new VictoryPoint(0), null);

    private VictoryPoint victoryPoint;
    private RegionType type;

    RegionCard(VictoryPoint victoryPoint, RegionType type) {
        this.victoryPoint = victoryPoint;
        this.type = type;
    }

    public VictoryPoint getRegionBonus() {
        return victoryPoint;
    }

    public RegionType getType() {
        return type;
    }

    @Override
    public String toString() {
        String regionCardString = type.name().toLowerCase();
        regionCardString = String.valueOf(Character.toUpperCase(regionCardString.charAt(0))) + regionCardString.substring(1);
        regionCardString = regionCardString + ", " + victoryPoint.toString();
        return regionCardString;
    }
}
