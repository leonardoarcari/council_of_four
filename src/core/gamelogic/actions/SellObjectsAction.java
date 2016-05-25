package core.gamelogic.actions;

import core.gamemodel.OnSaleItem;
import core.Player;

import java.util.Iterator;
import java.util.List;

/**
 * Created by Leonardo Arcari on 23/05/2016.
 */
public class SellObjectsAction extends Action implements MarketAction {
    private final List<OnSaleItem> onSaleItems;

    public SellObjectsAction(Player player, List<OnSaleItem> onSaleItems) {
        super(player);
        this.onSaleItems = onSaleItems;
    }

    public Iterator<OnSaleItem> itemsIterator() {
        return onSaleItems.iterator();
    }
}
