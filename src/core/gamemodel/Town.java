package core.gamemodel;

import core.gamemodel.bonus.Bonus;
import core.gamemodel.modelinterface.TownInterface;
import core.Player;
import server.Observer;
import server.Subject;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

/**
 * Created by Matteo on 20/05/16.
 */
public class Town implements Subject, TownInterface{
    private TownName townName;
    private TownType townType;
    private Vector<TownName> nearbyTowns;
    private List<Bonus> townBonus;
    private Vector<Player> playersEmporium;
    private boolean kingHere;

    private transient List<Observer> observers;

    public Town(TownName townName, List<Bonus> townBonus) {
        this.townName = townName;
        this.townBonus = townBonus;

        if(townName.equals(TownName.J)) {
            kingHere = true;
        } else {
            kingHere = false;
        }

        playersEmporium = new Vector<>();
        observers = new Vector<>();
        nearbyTowns = new Vector<>();
        townType = null;
    }

    public void setTownType() {
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

    public TownName getTownName() { return townName; }

    public void createEmporium(Player player) {
        playersEmporium.add(player);
        notifyObservers();
    }

    @Override
    public boolean hasEmporium(Player player) {
        return playersEmporium.contains(player);
    }

    @Override
    public Iterator<Bonus> bonusIterator() {
        return townBonus.iterator();
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
