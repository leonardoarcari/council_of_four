package core.gamelogic.actions;

import core.Player;
import core.gamemodel.OnSaleItem;

import java.util.List;

/**
 * This class represents the action to expose the object to sell
 * before the auction phase of the game.
 *
 * @see Action
 * @see MarketAction
 */
public class ExposeSellablesAction extends Action implements MarketAction {
    // Attribute of the action
    private List<OnSaleItem> onSaleItems;

    /**
     * @param player is the player doing the action
     * @param onSaleItems are the items to expose on the market
     */
    public ExposeSellablesAction(Player player, List<OnSaleItem> onSaleItems) {
        super(player);
        this.onSaleItems = onSaleItems;
    }

    /**
     * @return the list of items to put on sale
     */
    public List<OnSaleItem> getOnSaleItems() {
        return onSaleItems;
    }
}
