package core.gamemodel;

import javafx.scene.paint.Color;

import java.io.Serializable;

/**
 * This enumeration contains the types of the town in the game.
 */
public enum TownType implements Serializable {
    //Values of the enumeration
    GOLD(Color.GOLD),
    SILVER(Color.SILVER),
    BRONZE(Color.PERU),
    IRON(Color.DARKGRAY),
    KING(Color.VIOLET);

    // Attribute of the enumeration
    private final Color townType;

    /**
     * @param townType is one of the enumeration value
     */
    TownType(Color townType) {
        this.townType = townType;
    }

    /**
     * @return the type of the town
     */
    public Color getTownType() {
        return townType;
    }
}
