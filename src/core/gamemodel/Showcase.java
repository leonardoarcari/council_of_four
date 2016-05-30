package core.gamemodel;

import core.Observer;
import core.Player;
import core.Subject;
import core.gamemodel.modelinterface.SellableItem;

import java.util.Arrays;
import java.util.List;
import java.util.Vector;

/**
 * Created by Matteo on 29/05/16.
 */
public class Showcase implements Subject {
    private List<OnSaleItem> onSaleItems;
    private List<Player> players;

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
        onSaleItems.addAll(sellableItems);
        for(OnSaleItem onSale : sellableItems) {
            takeItemFrom(player, onSale);
        }
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
        onSaleItems.remove(acquiredItem);
        giveItemTo(player, acquiredItem);
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
        for(OnSaleItem leftover : onSaleItems) {
            Player owner = leftover.getOwner();
            removeItem(leftover, owner);
        }
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
