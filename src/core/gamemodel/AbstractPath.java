package core.gamemodel;

import core.Observer;
import core.Player;
import core.Subject;
import core.gamemodel.modelinterface.AbstractPathInterface;

import java.io.Serializable;
import java.util.*;

/**
 * This class implements two interfaces and their relative methods. From AbstractPathInterface
 * it inherits the path getters. The method movePlayer doesn't derive from the interface: this
 * because the interface is the only object exposed when communicating with the client (and
 * referring to the Façacde Pattern this implies that the client cannot access all of this
 * class methods but only the one described in the interface) and the movePlayer method is only
 * needed on the server side, when updating the model. From the Subject interface it inherits
 * all the observer modifier methods. This interface is part of the Observer Pattern: every
 * time a subject, that is a game object or even a player, is updated, the connections of
 * each player, which are "observing" the subjects, are notified, and send the updated objects
 * to the connected clients.
 */
public abstract class AbstractPath implements AbstractPathInterface, Subject, Serializable{
    // Attributes of the class
    protected List<List<Player>> players;
    private transient List<Observer> observers;

    public AbstractPath() {
        observers = new Vector<>();
    }

    /**
     * This method moves the player on the path by the given variation.
     * This cannot be negative, so the player cannot downgrade and the
     * player position cannot surpass the value 20
     *
     * @param player is the player to move
     * @param variation tells how many position the player has to move
     */
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

    /**
     * @param player is the player asking for his position
     * @see AbstractPathInterface
     * @return the player position
     */
    public int getPlayerPosition(Player player) {
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).contains(player)) {
                return i;
            }
        }
        throw new NoSuchElementException();
    }

    /**
     * @see AbstractPathInterface
     * @return a list of the players for each position of the path
     */
    @Override
    public List<List<Player>> getPlayers() {
        return players;
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

    /**
     * @see AbstractPathInterface
     * @return the podium
     */
    @Override
    public Map<Integer, List<Player>> getPodium() {
        Map<Integer, List<Player>> resultMap = new HashMap<>(2);
        int firstPosition = 0;
        int secondPosition = -1;
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).size() > 0) {
                secondPosition = firstPosition;
                firstPosition = i;
            }
        }
        resultMap.put(1, players.get(firstPosition));
        if(firstPosition != secondPosition)
            resultMap.put(2, players.get(secondPosition));
        return resultMap;
    }
}
