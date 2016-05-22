package Core.GameModel;

import Core.GameModel.ModelInterface.RegionInterface;
import Server.Observer;
import Server.Subject;

import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.Vector;

/**
 * Created by Matteo on 20/05/16. Need to add LinkTown method and attributes
 */
public class Region implements RegionInterface, Subject{
    boolean regionCardTaken;
    private CouncilorsBalcony regionBalcony;
    private Stack<PermitCard> regionPermitCards;
    private PermitCard rightPermitCard;
    private PermitCard leftPermitCard;
    private Vector<Town> regionTowns;
    private RegionCard regionCard;

    private transient List<Observer> observers;

    public Region(RegionCard regionCard, RegionType region) {
        this.regionCard = regionCard;
        createPermitCards(region);

        regionTowns = new Vector<>();
        regionPermitCards = new Stack<>();
        regionBalcony = new CouncilorsBalcony();
        regionCardTaken = false;

        observers = new Vector<>();
    }

    private void createPermitCards(RegionType region) {
        for(int i = 0; i < 15; i++) {
            regionPermitCards.add(new PermitCard(region));
        }
    }

    public void createRegionBalcony(Councilor[] councilors) {
        for(int i = 0; i < councilors.length; i++) {    //Length has to be 4
            regionBalcony.addCouncilor(councilors[i]);
        }
    }

    //TODO: Add checks on empty stack
    public PermitCard drawPermitCard(boolean isRight) {
        PermitCard drawn;
        if(isRight == PermitCard.RIGHT_CARD) {
            drawn = rightPermitCard;
            rightPermitCard = regionPermitCards.pop();
        } else {
            drawn = leftPermitCard;
            leftPermitCard = regionPermitCards.pop();
        }
        notifyObservers();
        return drawn;
    }

    public RegionCard drawRegionCard() throws AlreadyTakenException {
        if (!regionCardTaken) {
            regionCardTaken = true;
            notifyObservers(); // RegionCard must disappear from Region
            return regionCard;
        } else throw  new AlreadyTakenException();
    }

    @Override
    public boolean isRegionCardTaken() {
        return regionCardTaken;
    }

    @Override
    public PermitCard getLeftPermitCard() {
        return leftPermitCard;
    }

    @Override
    public PermitCard getRightPermitCard() {
        return rightPermitCard;
    }

    @Override
    public RegionCard getRegionCard() {
        if (!regionCardTaken) {
            return regionCard;
        } else return RegionCard.NULL;
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

    private class AlreadyTakenException extends RuntimeException {
        public AlreadyTakenException() {
            super();
        }

        public AlreadyTakenException(String message) {
            super(message);
        }
    }
}
