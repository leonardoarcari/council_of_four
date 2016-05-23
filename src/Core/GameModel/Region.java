package Core.GameModel;

import Core.GameModel.ModelInterface.RegionInterface;
import Server.Observer;
import Server.Subject;

import java.util.*;

/**
 * Created by Matteo on 20/05/16. Need to add LinkTown method and attributes
 */
public class Region implements RegionInterface, Subject{
    boolean regionCardTaken;
    private RegionType regionType;
    private CouncilorsBalcony regionBalcony;
    private Stack<PermitCard> regionPermitCards;
    private PermitCard rightPermitCard;
    private PermitCard leftPermitCard;
    private Vector<Town> regionTowns;
    private RegionCard regionCard;

    private transient List<Observer> observers;

    public Region(RegionCard regionCard, RegionType regionType, Councilor[] councilors) {

        // Region objects initialization
        this.regionCard = regionCard;
        this.regionType = regionType;
        createPermitCards(regionType);
        createRegionBalcony(councilors);
        setTowns();

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

    private void createRegionBalcony(Councilor[] councilors) {
        for(int i = 0; i < councilors.length; i++) {    //Length has to be 4
            regionBalcony.addCouncilor(councilors[i]);
        }
    }

    private void setTowns() {
        switch(regionType) {
            case SEA:
                regionTowns.addAll(new ArrayList<>(Arrays.asList(
                        new Town(TownName.A),
                        new Town(TownName.B),
                        new Town(TownName.C),
                        new Town(TownName.D),
                        new Town(TownName.E))));
                break;
            case HILLS:
                regionTowns.addAll(new ArrayList<>(Arrays.asList(
                        new Town(TownName.F),
                        new Town(TownName.G),
                        new Town(TownName.H),
                        new Town(TownName.I),
                        new Town(TownName.J))));
                break;
            case MOUNTAINS:
                regionTowns.addAll(new ArrayList<>(Arrays.asList(
                        new Town(TownName.K),
                        new Town(TownName.L),
                        new Town(TownName.M),
                        new Town(TownName.N),
                        new Town(TownName.O))));
                break;
        }
    }

    //TODO: Add checks on empty stack
    public PermitCard drawPermitCard(PermitPos pos) {
        PermitCard drawn;
        if(pos == PermitPos.RIGHT) {
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

    public Councilor updateBalcony(Councilor councilor) {
        return regionBalcony.addCouncilor(councilor);
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

    public enum PermitPos {
        LEFT, RIGHT
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
