package core.gamemodel;

import core.Observer;
import core.Player;
import core.Subject;
import core.gamemodel.modelinterface.SellableItem;
import core.gamemodel.modelinterface.ShowcaseInterface;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

/**
 * This class implements two interfaces and their relative methods. From ShowcaseInterface
 * it inherits the getters. Some methods, such as addItem, are not found in the interface: this
 * because the interface is the only object exposed when communicating with the client (and
 * referring to the Façacde Pattern this implies that the client cannot access all of this
 * class methods but only the one described in the interface) and those methods are only
 * needed on the server side, when updating the model. From the Subject interface it inherits
 * all the observer modifier methods. This interface is part of the Observer Pattern: every
 * time a subject, that is a game object or even a player, is updated, the connections of
 * each player, which are "observing" the subjects, are notified, and send the updated objects
 * to the connected clients.
 */
public class Showcase implements Subject, ShowcaseInterface, Serializable {
    // Attributes of the class
    private List<OnSaleItem> onSaleItems;
    private transient List<Player> players;

    private transient List<Observer> observers;

    public Showcase (){
        onSaleItems = new Vector<>();
        observers = new Vector<>();
        players = new Vector<>();
    }

    /**
     * @param player is the player to be added to the showcase
     */
    public void setPlayers(Player player) {
        players.add(player);
    }

    /**
     * This method removes items from a player and adds them to the showcase
     *
     * @param sellableItems are the items the player choose to sell
     * @param player is the player that wants to sell the items
     */
    public void addItems(List<OnSaleItem> sellableItems, Player player) {
        Player real = players.get(players.indexOf(player));
        onSaleItems.addAll(sellableItems);
        for (OnSaleItem onSale : sellableItems) {
            takeItemFrom(real, onSale);
        }
        notifyObservers();
    }

    private void takeItemFrom(Player player, OnSaleItem sellableItem) {
        SellableItem item = sellableItem.getItem();
        if(item.getClass().equals(PermitCard.class))
            player.removePermitCard((PermitCard) item);
        else if(item.getClass().equals(PoliticsCard.class))
            player.removePoliticsCard((PoliticsCard) item);
        else player.removeServant();
    }

    /**
     * This method removes an item from the showcase and adds it to the
     * player who purchased it
     *
     * @param acquiredItem the purchased item
     * @param player the player who purchased it
     */
    public void removeItem(OnSaleItem acquiredItem, Player player) {
        Player real = players.get(players.indexOf(player));
        onSaleItems.remove(acquiredItem);
        giveItemTo(real, acquiredItem);
        notifyObservers();
    }

    private void giveItemTo(Player player, OnSaleItem sellableItem) {
        SellableItem item = sellableItem.getItem();
        if(item.getClass().equals(PermitCard.class))
            player.addPermitCard((PermitCard) item);
        else if(item.getClass().equals(PoliticsCard.class))
            player.addPoliticsCard((PoliticsCard) item);
        else {
            Servant acquiredServant = (Servant) item;
            player.hireServants(Arrays.asList(acquiredServant));
        }
    }

    /**
     * This method occurs when the market phase ends end there are
     * some objects unsold. The method returns each of this items to
     * its original owner, acting as if he gained it.
     */
    public void returnItemsToOwner() {
        int onSaleItemsSize = onSaleItems.size();
        for (int i = 0; i < onSaleItemsSize; i++) {
            OnSaleItem leftover = onSaleItems.remove(0);
            Player owner = players.get(players.indexOf(leftover.getOwner()));
            giveItemTo(owner, leftover);
        }
        notifyObservers();
    }

    /**
     * @see ShowcaseInterface
     * @return the iterator of the showcase exposed items
     */
    @Override
    public Iterator<OnSaleItem> onSaleItemIterator() {
        return onSaleItems.iterator();
    }

    /**
     * @see Subject
     */
    @Override
    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    /**
     * @see Subject
     */
    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    /**
     * @see Subject
     */
    @Override
    public void notifyObservers() {
        for (Observer o : observers) {
            o.update(this);
        }
    }
}
