package core.gamemodel;

import core.Observer;
import core.Player;
import core.Subject;
import core.gamemodel.bonus.Bonus;
import core.gamemodel.modelinterface.TownInterface;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

/**
 * This class implements two interfaces and their relative methods. From TownInterface
 * it inherits the getters. Some methods, such as addNearby, is not found in the interface: this
 * because the interface is the only object exposed when communicating with the client (and
 * referring to the Façacde Pattern this implies that the client cannot access all of this
 * class methods but only the one described in the interface) and those methods are only
 * needed on the server side, when updating the model. From the Subject interface it inherits
 * all the observer modifier methods. This interface is part of the Observer Pattern: every
 * time a subject, that is a game object or even a player, is updated, the connections of
 * each player, which are "observing" the subjects, are notified, and send the updated objects
 * to the connected clients.
 */
public class Town implements Subject, TownInterface, Serializable{
    // Attributes of the class
    private TownName townName;
    private TownType townType;
    private List<TownName> nearbyTowns;
    private Bonus townBonus;
    private Vector<Player> playersEmporium;
    private boolean kingHere;

    private transient List<Observer> observers;

    /**
     * The constructor sets the towns info, as the king position and the type of the town
     *
     * @param townName is the name of the town
     * @param townBonus is the bonus of the town
     */
    public Town(TownName townName, Bonus townBonus) {
        this.townName = townName;
        this.townBonus = townBonus;
        kingHere = townName.equals(TownName.J);
        playersEmporium = new Vector<>();
        observers = new Vector<>();
        nearbyTowns = new Vector<>();
        setTownType();
    }

    /**
     * @param nearbyTowns are the name of the town linked to this
     */
    public void setNearbyTowns(List<TownName> nearbyTowns) {
        this.nearbyTowns = nearbyTowns;
    }

    /**
     * @param nearby is the name of the name of one linket town
     */
    public void addNearby(TownName nearby) {
        nearbyTowns.add(nearby);
    }

    private void setTownType() {
        switch (townName) {
            case A:case M:
                townType = TownType.IRON;
                break;
            case B:case F:case H:case K:case O:
                townType = TownType.GOLD;
                break;
            case C:case D:case G:case L:
                townType = TownType.SILVER;
                break;
            case E:case I:case N:
                townType = TownType.BRONZE;
                break;
            case J:
                townType = TownType.KING;
                break;
        }
    }

    /**
     * @see TownInterface
     */
    public Iterator<TownName> nearbiesIterator() {
        return nearbyTowns.iterator();
    }

    /**
     * @see TownInterface
     */
    @Override
    public boolean isKingHere() {
        return kingHere;
    }

    /**
     * @param here is whether the king should be set in this town
     */
    public void setKing (boolean here) {
        kingHere = here;
        notifyObservers();
    }

    /**
     * @see TownInterface
     */
    @Override
    public TownType getTownType() {
        return townType;
    }

    /**
     * @see TownInterface
     */
    @Override
    public TownName getTownName() { return townName; }

    /**
     * @param player is the emporium to create
     */
    public void createEmporium(Player player) {
        playersEmporium.add(player);
        notifyObservers();
    }

    /**
     * @see TownInterface
     */
    @Override
    public Iterator<Player> getPlayersEmporium() {
        return playersEmporium.iterator();
    }

    /**
     * @see TownInterface
     */
    @Override
    public boolean hasEmporium(Player player) {
        return playersEmporium.contains(player);
    }

    /**
     * @see TownInterface
     */
    @Override
    public int getEmporiumsNumber() {
        return playersEmporium.size();
    }

    /**
     * @see TownInterface
     */
    @Override
    public Bonus getTownBonus() {
        return townBonus;
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
