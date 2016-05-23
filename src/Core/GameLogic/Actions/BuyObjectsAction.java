package Core.GameLogic.Actions;

import Core.GameModel.OnSaleItem;
import Core.Player;

import java.util.Iterator;
import java.util.List;

/**
 * Created by Leonardo Arcari on 23/05/2016.
 */
public class BuyObjectsAction extends Action implements MarketAction {
    private final List<OnSaleItem> onSaleItems;

    public BuyObjectsAction(List<OnSaleItem> onSaleItems) {
        super();
        this.onSaleItems = onSaleItems;
    }

    public Iterator<OnSaleItem> itemsIterator() {
        return onSaleItems.iterator();
    }
}