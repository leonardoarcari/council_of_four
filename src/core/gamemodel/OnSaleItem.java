package core.gamemodel;

import core.Player;
import core.gamemodel.modelinterface.SellableItem;

import java.io.Serializable;
import java.util.Random;

/**
 * The OnSaleItem class acts as a wrapper of three other classes: PermitCard,
 * Servant and PoliticsCard. Those objects are the ones that can be sold
 * during the market phase. Hence they need a price and a way to identify the
 * owner. This class does that.
 */
public class OnSaleItem implements Serializable {
    // Attributes of the class
    private final String id;
    private final SellableItem item;
    private final int price;
    private final Player owner;

    /**
     * The constructor creates a sellable version of an object
     * selected by the player during the exposure phase of the market.
     *
     * @param item is the item that has to be sold
     * @param price is the price of the item
     * @param owner is the player that owned the object
     */
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

    /**
     * @return the item to be sold
     */
    public SellableItem getItem() {
        return item;
    }

    /**
     * @return the price of the item to be sold
     */
    public int getPrice() {
        return price;
    }

    /**
     * @return the owner of the item to be sold
     */
    public Player getOwner() {
        return owner;
    }

    /**
     * @param o the object to confront with
     * @return whether the confronted object are the same, based on the id of the item
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OnSaleItem that = (OnSaleItem) o;

        return id.equals(that.id);

    }

    /**
     * @return the hashCode of the item by its ID
     */
    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
