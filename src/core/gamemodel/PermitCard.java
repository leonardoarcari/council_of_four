package core.gamemodel;

import core.gamelogic.AbstractBonusFactory;
import core.gamelogic.TownNameFactory;
import core.gamemodel.bonus.Bonus;
import core.gamemodel.modelinterface.SellableItem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Created by Matteo on 16/05/16.
 */
public class PermitCard implements SellableItem, Serializable {
    private RegionType region;
    private boolean isBurned;
    private List<Bonus> bonuses;
    private List<TownName> townEmporiumPermit = new ArrayList<>();
    private int id;

    public PermitCard(RegionType region, AbstractBonusFactory factory, int cardId) {
        this.region = region;
        this.id = cardId;
        isBurned = false;
        bonuses = new ArrayList<>();

        bonuses = factory.generateBonuses();

        int townEmporiumPermitNumber = emporiumPermitCalculator();

        for(int i = 0; i < townEmporiumPermitNumber; i++) {
            ArrayList<TownName> supporter = new ArrayList<>(townEmporiumPermit);
            townEmporiumPermit.add(TownNameFactory.getTownName(supporter, region));
        }
    }

    private int emporiumPermitCalculator() {
        return new Random().nextInt(3) + 1;
    }

    public void burn() {
        isBurned = true;
    }

    public boolean isBurned() {
        return isBurned;
    }

    public List<Bonus> getBonuses() {
        return bonuses;
    }

    public Iterator<Bonus> getBonusesIterator() { return bonuses.iterator(); }

    public List<TownName> getCityPermits() { return townEmporiumPermit; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PermitCard that = (PermitCard) o;

        if (id != that.id) return false;
        return region == that.region;

    }

    @Override
    public int hashCode() {
        int result = region.hashCode();
        result = 31 * result + id;
        return result;
    }

    @Override
    public String toString() {
        String townPermits = "\tCan build in: [";
        String permitBonuses = "\tPermit bonus(es): [";
        for(TownName townName : townEmporiumPermit) {
            townPermits = townPermits + townName.name();
        }
        townPermits = townPermits +"]";
        int i = 0;
        for(Bonus bonus : bonuses) {
            permitBonuses = permitBonuses + bonus.toString();
            if(i != bonuses.size()-1) {
                permitBonuses = permitBonuses + ", ";
            }
            i++;
        }
        permitBonuses = permitBonuses + "]";

        return townPermits + "\n" + permitBonuses + "\n";
    }
}
