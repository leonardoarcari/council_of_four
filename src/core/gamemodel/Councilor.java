package core.gamemodel;

import java.io.Serializable;

/**
 * Created by Matteo on 20/05/16.
 */
public class Councilor implements Serializable {
    private static final long serialVersionUID = 1L;
    private final CouncilColor councilorColor;
    private final int id;

    public Councilor(CouncilColor councilorColor, int id) {
        this.councilorColor = councilorColor;
        this.id = id;
    }

    public CouncilColor getCouncilorColor() {
        return councilorColor;
    }

    @Override
    public String toString() {
        return councilorColor.name().charAt(0) + councilorColor.name().toLowerCase().substring(1);
    }

    @Override
    public boolean equals(Object councilor) {
        if (this == councilor) return true;
        if (councilor == null || getClass() != councilor.getClass()) return false;

        Councilor coun = (Councilor) councilor;

        return councilorColor.equals(coun.getCouncilorColor());
    }
}
