package core.gamemodel;

import core.Player;
import core.gamemodel.modelinterface.SellableItem;

/**
 * Created by Leonardo Arcari on 23/05/2016.
 */
public class OnSaleItem {
    private final SellableItem item;
    private final int price;
    private final Player owner;

    public OnSaleItem(SellableItem item, int price, Player owner) {
        this.item = item;
        this.price = (price <= 20) ? price : 20;
        this.owner = owner;
    }

    public SellableItem getItem() {
        return item;
    }

    public int getPrice() {
        return price;
    }

    public Player getOwner() {
        return owner;
    }
}
