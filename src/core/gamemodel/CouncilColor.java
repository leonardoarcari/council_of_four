package core.gamemodel;

import javafx.scene.paint.Color;

/**
 * Created by Matteo on 19/05/16.
 */
public enum CouncilColor {
    PURPLE(Color.PURPLE),
    BLACK(Color.BLACK),
    WHITE(Color.WHITE),
    CYAN(Color.CYAN),
    PINK(Color.PINK),
    ORANGE(Color.ORANGE),
    RAINBOW(Color.TRANSPARENT);

    private final Color color;

    CouncilColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}