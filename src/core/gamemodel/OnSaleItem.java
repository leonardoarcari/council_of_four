package core.gamemodel;

import core.Player;
import core.gamemodel.modelinterface.SellableItem;

import java.io.Serializable;
import java.util.Random;

/**
 * Created by Leonardo Arcari on 23/05/2016.
 */
public class OnSaleItem implements Serializable {
    private final String id;
    private final SellableItem item;
    private final int price;
    private final Player owner;

    public OnSaleItem(SellableItem item, int price, Player owner) {
        id = generateID();
        this.item = item;
        this.price = (price <= 20) ? price : 20;
        this.owner = owner;
    }

    private String generateID() {
        String id = "";
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            id += random.nextInt(10);
        }
        return id;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OnSaleItem that = (OnSaleItem) o;

        return id.equals(that.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
