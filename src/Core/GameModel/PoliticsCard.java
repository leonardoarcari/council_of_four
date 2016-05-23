package Core.GameModel;

import Core.GameModel.ModelInterface.SellableItem;

/**
 * Created by Matteo on 20/05/16.
 */
public class PoliticsCard implements SellableItem{
    private CouncilColor cardColor;

    public PoliticsCard(CouncilColor cardColor) {
        this.cardColor = cardColor;
    }

    public CouncilColor getCardColor() {
        return cardColor;
    }
}
