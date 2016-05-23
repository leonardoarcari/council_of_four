package Core.GameModel;

import Core.GameLogic.BonusFactory;
import Core.GameModel.Bonus.Bonus;
import Core.GameModel.Bonus.BonusNumber;
import Core.GameModel.ModelInterface.TownInterface;
import Core.Player;
import Server.Observer;
import Server.Subject;

import java.util.List;
import java.util.Vector;

/**
 * Created by Matteo on 20/05/16.
 */
public class Town implements Subject, TownInterface{
    private TownName townName;
    private TownType townType;
    private Vector<Town> nearbyTowns;
    private Bonus townBonus;
    private Vector<Player> playersEmporium;
    private boolean kingHere;

    private transient List<Observer> observers;

    public Town(TownName townName) {
        this.townName = townName;
        this.townBonus = BonusFactory.createBonus(BonusNumber.ONE_PROBABILITY);

        if(townName.equals(TownName.J)) {
            kingHere = true;
        } else {
            kingHere = false;
        }

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

    public void addNearbyTown(Town nearbyTown) {
        nearbyTowns.add(nearbyTown);
    }

    @Override
    public boolean isKingHere() {
        return kingHere;
    }

    public void setKingHere() {
        kingHere = true;
        notifyObservers();
    }

    @Override
    public TownType getTownType() {
        return townType;
    }

    public void createEmporium(Player player) {
        playersEmporium.add(player);
        notifyObservers();
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
