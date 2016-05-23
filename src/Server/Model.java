package Server;

import Core.Connection.InfoProcessor;
import Core.Connection.RMIProcessor;
import Core.ModelInterface;
import Core.Player;
import Server.ServerConnection.RMIGameProcessor;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Created by Leonardo Arcari on 20/05/2016.
 */
public class Model implements ModelInterface, Runnable, Serializable, Subject{
    private static final long serialVersionUID = 1L;
    private transient ArrayList<Observer> observers;
    private transient InfoProcessor processor;

    private String usefulStuff;

    public Model() {
        observers = new ArrayList<>();
        usefulStuff = "";
        processor = new ServerProcessor(this);
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

    @Override
    public String getUsefulStuff() {
        return usefulStuff;
    }

    @Override
    public void run() {
        for (Player p : WaitingHall.getInstance().pullPlayers()) {
            usefulStuff = usefulStuff + p.getUsername() + " @ " + p.getNickname() + "\n";
        }
        WaitingHall.getInstance().incrementGamesCounter();
        notifyObservers();
    }

    public InfoProcessor getProcessor() {
        return processor;
    }
}
