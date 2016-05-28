package core.gamelogic.actions;

import core.Player;
import core.gamemodel.PermitCard;

/**
 * Created by Matteo on 28/05/16.
 */
public class TakePermitBonusAction extends Action implements SpecialAction {
    PermitCard myPermitCard;

    public TakePermitBonusAction(Player player, PermitCard permitCard) {
        super(player);
        this.myPermitCard = permitCard;
    }

    public PermitCard getMyPermitCard() {
        return myPermitCard;
    }
}
