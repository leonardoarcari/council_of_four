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
 * Created by Matteo on 20/05/16.
 */
public class Town implements Subject, TownInterface, Serializable{
    private TownName townName;
    private TownType townType;
    private List<TownName> nearbyTowns;
    private Bonus townBonus;
    private Vector<Player> playersEmporium;
    private boolean kingHere;
    private transient List<Observer> observers;

    public Town(TownName townName, Bonus townBonus) {
        this.townName = townName;
        this.townBonus = townBonus;
        kingHere = townName.equals(TownName.J);
        playersEmporium = new Vector<>();
        observers = new Vector<>();
        nearbyTowns = new Vector<>();
        setTownType();
    }

    public void setNearbyTowns(List<TownName> nearbyTowns) {
        this.nearbyTowns = nearbyTowns;
    }

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

    public Iterator<TownName> nearbiesIterator() {
        return nearbyTowns.iterator();
    }

    @Override
    public boolean isKingHere() {
        return kingHere;
    }

    public void setKing (boolean here) {
        kingHere = here;
        notifyObservers();
    }

    @Override
    public TownType getTownType() {
        return townType;
    }

    @Override
    public TownName getTownName() { return townName; }

    public void createEmporium(Player player) {
        playersEmporium.add(player);
        notifyObservers();
    }

    @Override
    public Iterator<Player> getPlayersEmporium() {
        return playersEmporium.iterator();
    }

    @Override
    public boolean hasEmporium(Player player) {
        return playersEmporium.contains(player);
    }

    @Override
    public Bonus getTownBonus() {
        return townBonus;
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
