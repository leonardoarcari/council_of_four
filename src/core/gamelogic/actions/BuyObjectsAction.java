package core.gamelogic.actions;

import core.Player;
import core.gamemodel.OnSaleItem;

import java.util.Iterator;
import java.util.List;

/**
 * Created by Leonardo Arcari on 23/05/2016.
 */
public class BuyObjectsAction extends Action implements MarketAction {
    private final List<OnSaleItem> onSaleItems;

    public BuyObjectsAction(Player player, List<OnSaleItem> onSaleItems) {
        super(player);
        this.onSaleItems = onSaleItems;
    }

    public Iterator<OnSaleItem> itemsIterator() {
        return onSaleItems.iterator();
    }
}