package core.gamelogic.actions;

import core.Player;
import core.gamemodel.PermitCard;

/**
 * Created by Matteo on 25/05/16.
 */
public class SelectAgainPermitAction extends Action implements BonusAction{
    private PermitCard permitCard;

    public SelectAgainPermitAction(Player player, PermitCard permitCard) {
        super(player);
        this.permitCard = permitCard;
    }

    public PermitCard getPermitCard() {
        return permitCard;
    }
}
