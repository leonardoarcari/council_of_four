package core.gamemodel;

import javafx.scene.paint.Color;

import java.io.Serializable;

/**
 * This enumeration gives a set of colors for the Councilor objects.
 */
public enum CouncilColor implements Serializable{
    // Values of the enumeration
    PURPLE(Color.PURPLE),
    BLACK(Color.BLACK),
    WHITE(Color.WHITE),
    CYAN(Color.CYAN),
    PINK(Color.PINK),
    ORANGE(Color.ORANGE),
    RAINBOW(Color.TRANSPARENT);

    // Attribute identifier of the object
    private final Color color;

    CouncilColor(Color color) {
        this.color = color;
    }

    /**
     * @return the color of the Councilor on which this method is invoked
     */
    public Color getColor() {
        return color;
    }
}
