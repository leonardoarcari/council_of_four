package core.gamelogic.actions;

import core.gamemodel.PoliticsCard;

/**
 * Created by Matteo on 23/05/16.
 */
public class DrawCardAction implements SyncAction{
    PoliticsCard newCard;

    public DrawCardAction(PoliticsCard newCard) {
        this.newCard = newCard;
    }

    public PoliticsCard getNewCard() {
        return newCard;
    }
}
