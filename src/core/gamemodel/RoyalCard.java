package core.gamemodel;

import core.gamemodel.bonus.VictoryPoint;

import java.io.Serializable;

/**
 * The enumeration represents one of the set of bonus cards of the game, the royal cards, identified
 * by their fixed value.
 */
public enum RoyalCard implements Serializable {
    // Values of the enumeration
    FIRST(new VictoryPoint(25)),
    SECOND(new VictoryPoint(18)),
    THIRD(new VictoryPoint(12)),
    FOURTH(new VictoryPoint(7)),
    FIFTH(new VictoryPoint(3));

    // Attribute of the enumeration
    private VictoryPoint victoryPoint;

    /**
     * @param victoryPoint is the value of the card
     */
    RoyalCard(VictoryPoint victoryPoint) {
        this.victoryPoint = victoryPoint;
    }

    /**
     * @return the bonus of the card, that is always a VictoryPoint bonus
     */
    public VictoryPoint getRoyalBonus() {
        return victoryPoint;
    }

    /**
     * @return a string formed by the name of the card and its value
     */
    @Override
    public String toString() {
        String royalCardString = this.name().toLowerCase();
        royalCardString = String.valueOf(Character.toUpperCase(royalCardString.charAt(0))) + royalCardString.substring(1);
        royalCardString = royalCardString + ", " + victoryPoint.toString();
        return royalCardString;
    }
}
