package core.gamemodel;

import javafx.scene.paint.Color;

import java.io.Serializable;

/**
 * Created by Matteo on 20/05/16.
 */
public enum TownType implements Serializable {
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
