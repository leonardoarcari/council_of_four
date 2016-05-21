package Core.GameModel;

/**
 * Created by Matteo on 20/05/16.
 */
public class Councilor {
    private final CouncilColor councilorColor;

    public Councilor(CouncilColor councilorColor) {
        this.councilorColor = councilorColor;
    }

    public CouncilColor getCouncilorColor() {
        return councilorColor;
    }
}
