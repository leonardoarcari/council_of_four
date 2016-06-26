package core.gamelogic.actions;

import core.Player;
import core.gamemodel.PermitCard;

/**
 * This class represents one of the special actions gained from
 * the nobility path; it allows the player to gain again the bonuses
 * of one of his permit cards.
 *
 * @see Action
 * @see SpecialAction
 */
public class TakePermitBonusAction extends Action implements SpecialAction {
    // Is the selected permit card
    private PermitCard myPermitCard;

    /**
     * @param player is the player doing the action
     * @param permitCard is the selected permit card
     */
    public TakePermitBonusAction(Player player, PermitCard permitCard) {
        super(player);
        this.myPermitCard = permitCard;
    }

    /**
     * @return the selected permit card
     */
    public PermitCard getMyPermitCard() {
        return myPermitCard;
    }
}
