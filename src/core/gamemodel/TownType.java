package core.gamemodel;

import javafx.scene.paint.Color;

/**
 * Created by Matteo on 20/05/16.
 */
public enum TownType {
    GOLD(Color.GOLD),
    SILVER(Color.SILVER),
    BRONZE(Color.PERU),
    IRON(Color.DARKGRAY),
    KING(Color.VIOLET);

    private final Color townType;

    TownType(Color townType) {
        this.townType = townType;
    }

    public Color getTownType() {
        return townType;
    }
}
