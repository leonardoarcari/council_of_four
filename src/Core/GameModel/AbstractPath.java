package Core.GameModel;

import Core.Player;
import Server.Observer;
import Server.Subject;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by Matteo on 23/05/16.
 */
public class AbstractPath implements Subject{
    protected List<List<Player>> players;
    protected transient List<Observer> observers;

    public AbstractPath() {
        observers = new Vector<>();
    }

    public void movePlayer(Player player, int variation) {
        for (int i = 0; i < players.size(); i++) {
            if(players.get(i).contains(player)) {
                players.get(i).remove(player);
                int newPos = (i+variation < 20) ? i+variation : 20;
                players.get(newPos).add(player);
                notifyObservers();
                break;
            }
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
