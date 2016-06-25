package core.gamemodel;

import java.io.Serializable;

/**
 * This class represents the councilor object of the game.
 */
public class Councilor implements Serializable {
    // Attribute used during serialization
    private static final long serialVersionUID = 1L;

    // Attributes of the class
    private final CouncilColor councilorColor;
    private final int id;

    public Councilor(CouncilColor councilorColor, int id) {
        this.councilorColor = councilorColor;
        this.id = id;
    }

    /**
     * @return the color of the councilor
     */
    public CouncilColor getCouncilorColor() {
        return councilorColor;
    }

    /**
     * @return the color name which identifies the object
     */
    @Override
    public String toString() {
        return councilorColor.name().charAt(0) + councilorColor.name().toLowerCase().substring(1);
    }

    /**
     * @param councilor is the object to confront with
     * @return whether the calling object and the councilor are the same object or not, comparing their colors
     */
    @Override
    public boolean equals(Object councilor) {
        if (this == councilor) return true;
        if (councilor == null || getClass() != councilor.getClass()) return false;

        Councilor coun = (Councilor) councilor;

        return councilorColor.equals(coun.getCouncilorColor());
    }
}
