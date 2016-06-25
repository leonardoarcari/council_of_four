package core.gamemodel;

import core.gamemodel.bonus.VictoryPoint;

import java.io.Serializable;

/**
 * The enumeration represents one of the set of bonus cards of the game, the town type
 * cards, identified by their fixed value and one of type of the town.
 */
public enum TownTypeCard implements Serializable {
    // Values of the enumeration
    GOLD(new VictoryPoint(20), TownType.GOLD),
    SILVER(new VictoryPoint(12), TownType.SILVER),
    BRONZE(new VictoryPoint(8), TownType.BRONZE),
    IRON(new VictoryPoint(5), TownType.IRON);

    // Attributes of the enumeration
    private VictoryPoint victoryPoint;
    private TownType townType;

    /**
     * @param victoryPoint is the bonus of the card
     * @param townType is the town type of the card
     */
    TownTypeCard(VictoryPoint victoryPoint, TownType townType) {
        this.victoryPoint = victoryPoint;
        this.townType = townType;
    }

    /**
     * @return the bonus of the card
     */
    public VictoryPoint getTypeBonus() {
        return victoryPoint;
    }

    /**
     * @return the town type od the card
     */
    public TownType getTownType() {
        return townType;
    }

    /**
     * @return a string formed by the name of the card and its value
     */
    @Override
    public String toString() {
        String townCardString = townType.name().toLowerCase();
        townCardString = String.valueOf(Character.toUpperCase(townCardString.charAt(0))) + townCardString.substring(1);
        townCardString = townCardString + ", " + victoryPoint.toString();
        return townCardString;
    }
}
