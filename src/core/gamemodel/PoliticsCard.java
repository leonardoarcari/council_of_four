package core.gamemodel;

import core.gamemodel.modelinterface.SellableItem;

import java.io.Serializable;
import java.util.Random;

/**
 * This class represent the Politics Card object of the game. It's one
 * of the item that can be sold during the market phase, so it implements
 * the SellableItem interface.
 */
public class PoliticsCard implements SellableItem, Serializable {
    // Attributes of the class
    private CouncilColor cardColor;
    private String id;

    /**
     * @param cardColor is the color of the card
     */
    public PoliticsCard(CouncilColor cardColor) {
        id = generateID();
        this.cardColor = cardColor;
    }

    private String generateID() {
        String id = "";
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            id += random.nextInt(10);
        }
        return id;
    }

    /**
     * @return the color of the card
     */
    public CouncilColor getCardColor() {
        return cardColor;
    }

    /**
     * @param o the object to confront with
     * @return whether the confronted object are the same, based on the id of the item
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PoliticsCard that = (PoliticsCard) o;

        return id.equals(that.id);

    }

    /**
     * @return the hash code of the card by its ID
     */
    @Override
    public int hashCode() {
        return id.hashCode();
    }

    /**
     * @return the string representing the color of the card
     */
    @Override
    public String toString() {
        return cardColor.name();
    }
}
