package core.gamelogic.actions;

import core.Player;
import core.gamemodel.OnSaleItem;

import java.util.Iterator;
import java.util.List;

/**
 * This class represents the market action that allows the player to
 * buy the selected item during the auction phase of the game.
 *
 * @see Action
 * @see MarketAction
 */
public class BuyObjectsAction extends Action implements MarketAction {
    // Attribute of the action
    private final List<OnSaleItem> onSaleItems;

    /**
     * @param player is the player requesting tu buy some objects
     * @param onSaleItems is the list of items to buy
     */
    public BuyObjectsAction(Player player, List<OnSaleItem> onSaleItems) {
        super(player);
        this.onSaleItems = onSaleItems;
    }

    /**
     * @return the iterator of the items to buy
     */
    public Iterator<OnSaleItem> itemsIterator() {
        return onSaleItems.iterator();
    }
}