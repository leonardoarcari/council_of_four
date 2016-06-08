package core.gamemodel;

import javafx.scene.paint.Color;

import java.io.Serializable;

/**
 * Created by Leonardo Arcari on 05/06/2016.
 */
public enum PlayerColor implements Serializable{
    AQUA(Color.AQUA),
    AQUAMARINE(Color.AQUAMARINE),
    BLUE(Color.BLUE),
    BLUEVIOLET(Color.BLUEVIOLET),
    CHARTREUSE(Color.CHARTREUSE),
    CRIMSON(Color.CRIMSON),
    DARKGREEN(Color.DARKGREEN),
    FUCHSIA(Color.FUCHSIA),
    LAWGREEN(Color.LAWNGREEN),
    LIGHTSLATEGRAY(Color.LIGHTSLATEGRAY),
    YELLOW(Color.YELLOW);

    private final Color color;

    PlayerColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}
