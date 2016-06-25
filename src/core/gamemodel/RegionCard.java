package core.gamemodel;

import core.gamemodel.bonus.VictoryPoint;

import java.io.Serializable;

/**
 * The enumeration represents one of the set of bonus cards of the game, the region cards, identified
 * by their fixed value and the type of the region owning it.
 */
public enum RegionCard implements Serializable {
    // Values of the enumeration
    SEA(new VictoryPoint(5), RegionType.SEA),
    HILLS(new VictoryPoint(5), RegionType.HILLS),
    MOUNTAINS(new VictoryPoint(5), RegionType.MOUNTAINS),
    NULL(new VictoryPoint(0), null);

    // Attributes of the enumeration
    private VictoryPoint victoryPoint;
    private RegionType type;

    /**
     * @param victoryPoint is the bonus of the card
     * @param type is the type of the region owning the card
     */
    RegionCard(VictoryPoint victoryPoint, RegionType type) {
        this.victoryPoint = victoryPoint;
        this.type = type;
    }

    /**
     * @return the bonus, that is a VictoryPoint, of the card
     */
    public VictoryPoint getRegionBonus() {
        return victoryPoint;
    }

    /**
     * @return the type of the region owning the card
     */
    public RegionType getType() {
        return type;
    }

    /**
     * @return a string formed by the name of the card and its value
     */
    @Override
    public String toString() {
        String regionCardString = type.name().toLowerCase();
        regionCardString = String.valueOf(Character.toUpperCase(regionCardString.charAt(0))) + regionCardString.substring(1);
        regionCardString = regionCardString + ", " + victoryPoint.toString();
        return regionCardString;
    }
}
