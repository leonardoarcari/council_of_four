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
 * Created by Matteo on 29/05/16.
 */
public class Showcase implements Subject, ShowcaseInterface, Serializable {
    private List<OnSaleItem> onSaleItems;
    private transient List<Player> players;

    private transient List<Observer> observers;

    public Showcase (){
        onSaleItems = new Vector<>();
        observers = new Vector<>();
        players = new Vector<>();
    }

    public void setPlayers(Player player) {
        players.add(player);
    }

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

    public void returnItemsToOwner() {
        for (int i = 0; i < onSaleItems.size(); i++) {
            OnSaleItem leftover = onSaleItems.remove(0);
            Player owner = players.get(players.indexOf(leftover.getOwner()));
            giveItemTo(owner, leftover);
        }
        notifyObservers();
    }

    @Override
    public Iterator<OnSaleItem> onSaleItemIterator() {
        return onSaleItems.iterator();
    }

    @Override
    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer o : observers) {
            o.update(this);
        }
    }
}
