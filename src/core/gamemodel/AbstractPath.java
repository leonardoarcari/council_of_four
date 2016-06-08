package core.gamemodel;

import core.Observer;
import core.Player;
import core.Subject;
import core.gamemodel.modelinterface.AbstractPathInterface;

import java.io.Serializable;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Vector;

/**
 * Created by Matteo on 23/05/16.
 */
public abstract class AbstractPath implements AbstractPathInterface, Subject, Serializable{
    protected List<List<Player>> players;
    private transient List<Observer> observers;

    public AbstractPath() {
        observers = new Vector<>();
    }

    public void movePlayer(Player player, int variation) {
        if (variation <= 0 || player == null) {
            throw new IllegalArgumentException();
        }
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).contains(player)) {
                players.get(i).remove(player);
                int newPos = (i+variation < 20) ? i+variation : 20;
                players.get(newPos).add(player);
                notifyObservers();
                return;
            }
        }
        throw new NoSuchElementException();
    }

    public int getPlayerPosition(Player player) {
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).contains(player)) {
                return i;
            }
        }
        throw new NoSuchElementException();
    }

    @Override
    public List<List<Player>> getPlayers() {
        return players;
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
