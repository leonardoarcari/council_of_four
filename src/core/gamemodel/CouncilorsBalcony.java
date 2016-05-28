package core.gamemodel;

import core.gamemodel.modelinterface.BalconyInterface;
import core.Observer;
import core.Subject;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Vector;

/**
 * Created by Matteo on 20/05/16.
 */
public class CouncilorsBalcony implements BalconyInterface, Subject{
    private Councilor[] councilorsBalcony;
    private final RegionType region;
    private transient List<Observer> observers;

    public CouncilorsBalcony(RegionType regionType) {
        councilorsBalcony = new Councilor[4];
        this.region = regionType;
        observers = new Vector<>();
    }

    /**
     * Clean punch in the balls function
     */
    public Councilor addCouncilor(Councilor councilor) {
        Councilor councilorToAddToPool = councilorsBalcony[3];
        for(int i = 3; i > 0; i--) {
            councilorsBalcony[i] = councilorsBalcony[i-1];
        }
        councilorsBalcony[0] = councilor;
        notifyObservers();
        return councilorToAddToPool;
    }

    public RegionType getRegion() {
        return region;
    }

    @Override
    public Iterator<Councilor> councilorsIterator() {
        return new CouncilorIterator();
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
    public String toString() {
        String toString = "";
        for (int i = 0; i < councilorsBalcony.length; i++) {
            toString = toString + councilorsBalcony[i] + " ";
        }
        return toString;
    }

    private class CouncilorIterator implements Iterator<Councilor> {
        private int current = 0;

        @Override
        public boolean hasNext() {
            return current < 4;
        }

        @Override
        public Councilor next() {
            if (hasNext()) {
                return councilorsBalcony[current++];
            } else throw new NoSuchElementException();
        }
    }
}
