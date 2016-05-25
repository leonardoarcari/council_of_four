package core.gamemodel;

/**
 * Created by Matteo on 20/05/16.
 */
public class Councilor {
    private final CouncilColor councilorColor;
    private final int id;

    public Councilor(CouncilColor councilorColor, int id) {
        this.councilorColor = councilorColor;
        this.id = id;
    }

    public CouncilColor getCouncilorColor() {
        return councilorColor;
    }
}
