package Core.GameLogic.Actions;

import Core.GameModel.PoliticsCard;

/**
 * Created by Matteo on 23/05/16.
 */
public class DrawCardAction {
    PoliticsCard newCard;

    public DrawCardAction(PoliticsCard newCard) {
        this.newCard = newCard;
    }

    public PoliticsCard getNewCard() {
        return newCard;
    }
}
