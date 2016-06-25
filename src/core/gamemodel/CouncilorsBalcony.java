package core.gamemodel;

import core.Observer;
import core.Subject;
import core.gamemodel.modelinterface.BalconyInterface;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Vector;

/**
 * This class implements two interfaces and their relative methods. From BalconyInterface
 * it inherits the balcony getters. The method addCouncilor doesn't derive from the interface: this
 * because the interface is the only object exposed when communicating with the client (and
 * referring to the Façacde Pattern this implies that the client cannot access all of this
 * class methods but only the one described in the interface) and the addCouncilor method is only
 * needed on the server side, when updating the model. From the Subject interface it inherits
 * all the observer modifier methods. This interface is part of the Observer Pattern: every
 * time a subject, that is a game object or even a player, is updated, the connections of
 * each player, which are "observing" the subjects, are notified, and send the updated objects
 * to the connected clients.
 */
public class CouncilorsBalcony implements BalconyInterface, Subject, Serializable{
    // Attributes of the class
    private Councilor[] councilorsBalcony;
    private final RegionType region;

    private transient List<Observer> observers;

    public CouncilorsBalcony(RegionType regionType) {
        councilorsBalcony = new Councilor[4];
        this.region = regionType;
        observers = new Vector<>();
    }

    /**
     * This method adds a councilor to the balcony, emulating a FIFO queue.
     * The new councilor is added to the left side of the balcony, and the
     * right-most one exits from the balcony.
     *
     * @param councilor is the inserted councilor
     * @return the right-most councilor, that goes out of the balcony
     */
    public Councilor addCouncilor(Councilor councilor) {
        Councilor councilorToAddToPool = councilorsBalcony[3];
        System.arraycopy(councilorsBalcony, 0, councilorsBalcony, 1, 3);
        councilorsBalcony[0] = councilor;
        notifyObservers();
        return councilorToAddToPool;
    }

    /**
     * @see BalconyInterface
     */
    @Override
    public RegionType getRegion() {
        return region;
    }

    /**
     * @see BalconyInterface
     */
    @Override
    public Iterator<Councilor> councilorsIterator() {
        return new CouncilorIterator();
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
     * This method builds a string containing the balcony's councilors information
     *
     * @return a string that shows which councilors are part of the balcony
     */
    @Override
    public String toString() {
        String toString = "";
        for (Councilor aCouncilorsBalcony : councilorsBalcony) {
            toString = toString + aCouncilorsBalcony + " ";
        }
        return toString;
    }

    /**
     * This method builds a string containing the balcony's information
     *
     * @return a string that shows the balcony status, such as region and councilors,
     * in a formatted way (including parenthesis and spaces)
     */
    @Override
    public String toFormattedString() {
        String regionString = region.name().toLowerCase();
        regionString = String.valueOf(Character.toUpperCase(regionString.charAt(0))) + regionString.substring(1);

        StringBuilder builder = new StringBuilder(regionString);
        builder.append("'s balcony [");
        for (int i = 0; i < councilorsBalcony.length; i++) {
            builder.append(councilorsBalcony[i])
                   .append((i != councilorsBalcony.length - 1) ? ", " : "");
        }
        builder.append("]");
        return builder.toString();
    }

    /**
     * This inner class represents the iterator of councilors of the balcony
     */
    private class CouncilorIterator implements Iterator<Councilor> {
        private int current = 0;

        /**
         * @return whether the iterator has more elements to iterate on
         */
        @Override
        public boolean hasNext() {
            return current < 4;
        }

        /**
         * @return the councilor pointed by the iterator
         */
        @Override
        public Councilor next() {
            if (hasNext()) {
                return councilorsBalcony[current++];
            } else throw new NoSuchElementException();
        }
    }
}
