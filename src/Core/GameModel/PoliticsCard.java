package Core.GameModel;

/**
 * Created by Matteo on 20/05/16.
 */
public class PoliticsCard {
    private CouncilColor cardColor;

    public PoliticsCard(CouncilColor cardColor) {
        this.cardColor = cardColor;
    }

    public CouncilColor getCardColor() {
        return cardColor;
    }
}
