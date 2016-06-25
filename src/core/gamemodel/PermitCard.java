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
 * This class represents the Permit Card object of the game. It's one
 * of the item that can be sold during the market phase, so it implements
 * the SellableItem interface.
 */
public class PermitCard implements SellableItem, Serializable {
    // Attributes of the class
    private RegionType region;
    private boolean isBurned;
    private List<Bonus> bonuses;
    private List<TownName> townEmporiumPermit = new ArrayList<>();
    private int id;

    /**
     * The constructor creates the bonuses and town names of the card using the
     * specific bonus factory and the static method of the TownNameFactory class
     *
     * @param region is the region that owns the permit card
     * @param factory is the factory that creates the bonus of the card
     * @param cardId is the ID of the card
     */
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

    /**
     * This method marks the permit card as already used by a player,
     * "burning" it
     */
    public void burn() {
        isBurned = true;
    }

    /**
     * @return whether the card has already been used or not
     */
    public boolean isBurned() {
        return isBurned;
    }

    /**
     * @return the list of bonuses of the card
     */
    public List<Bonus> getBonuses() {
        return bonuses;
    }

    /**
     * @return the iterator on the card bonuses
     */
    public Iterator<Bonus> getBonusesIterator() { return bonuses.iterator(); }

    /**
     * @return the list of the name of the towns where the player can build an
     * emporium thanks to this card
     */
    public List<TownName> getCityPermits() { return townEmporiumPermit; }

    /**
     * @param o the object to confront with
     * @return whether the confronted object are the same, based on the id of the item
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PermitCard that = (PermitCard) o;

        if (id != that.id) return false;
        return region == that.region;
    }

    /**
     * @return the hashCode of the permit card, given the owning region code
     */
    @Override
    public int hashCode() {
        int result = region.hashCode();
        result = 31 * result + id;
        return result;
    }

    /**
     * @return a formatted string that contains the card bonuses and town names
     */
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
