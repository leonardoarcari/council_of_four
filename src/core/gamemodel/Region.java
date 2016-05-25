package core.gamemodel;

import core.Player;
import core.gamelogic.AbstractBonusFactory;
import core.gamelogic.BonusFactory;
import core.gamelogic.BonusOwner;
import core.gamemodel.bonus.Bonus;
import core.gamemodel.modelinterface.RegionInterface;
import server.Observer;
import server.Subject;

import java.util.*;

/**
 * Created by Matteo on 20/05/16. Need to add LinkTown method and attributes
 */
public class Region implements RegionInterface, Subject{
    boolean regionCardTaken;
    private RegionType regionType;
    private CouncilorsBalcony regionBalcony;
    private List<PermitCard> regionPermitCards;
    private PermitCard rightPermitCard;
    private PermitCard leftPermitCard;
    private List<Town> regionTowns;
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
        regionPermitCards = new Vector<>();
        regionBalcony = new CouncilorsBalcony();
        regionCardTaken = false;

        observers = new Vector<>();
    }

    private void createPermitCards(RegionType region) {
        AbstractBonusFactory bonusFactory = BonusFactory.getFactory(BonusOwner.PERMIT);
        for(int i = 0; i < 15; i++) {
            List<Bonus> bonuses = bonusFactory.generateBonuses();
            regionPermitCards.add(new PermitCard(region, bonuses, i));
        }
    }

    private void createRegionBalcony(Councilor[] councilors) {
        for(int i = 0; i < councilors.length; i++) {    //Length has to be 4
            regionBalcony.addCouncilor(councilors[i]);
        }
    }

    private void setTowns() {
        AbstractBonusFactory bonusFactory = BonusFactory.getFactory(BonusOwner.TOWN);
        switch(regionType) {
            case SEA:
                regionTowns.addAll(new ArrayList<>(Arrays.asList(
                        new Town(TownName.A, bonusFactory.generateBonuses()),
                        new Town(TownName.B, bonusFactory.generateBonuses()),
                        new Town(TownName.C, bonusFactory.generateBonuses()),
                        new Town(TownName.D, bonusFactory.generateBonuses()),
                        new Town(TownName.E, bonusFactory.generateBonuses()))));
                break;
            case HILLS:
                regionTowns.addAll(new ArrayList<>(Arrays.asList(
                        new Town(TownName.F, bonusFactory.generateBonuses()),
                        new Town(TownName.G, bonusFactory.generateBonuses()),
                        new Town(TownName.H, bonusFactory.generateBonuses()),
                        new Town(TownName.I, bonusFactory.generateBonuses()),
                        new Town(TownName.J, bonusFactory.generateBonuses()))));
                break;
            case MOUNTAINS:
                regionTowns.addAll(new ArrayList<>(Arrays.asList(
                        new Town(TownName.K, bonusFactory.generateBonuses()),
                        new Town(TownName.L, bonusFactory.generateBonuses()),
                        new Town(TownName.M, bonusFactory.generateBonuses()),
                        new Town(TownName.N, bonusFactory.generateBonuses()),
                        new Town(TownName.O, bonusFactory.generateBonuses()))));
                break;
        }
    }

    public void buildEmporium(Player player, TownName town) {
        getTownByName(town).createEmporium(player);
    }

    public Town getTownByName(TownName townName) {
        for(Town town: regionTowns) {
            if(town.getTownName().equals(townName))
                return town;
        }
        throw new NoSuchElementException();
    }

    //TODO: Add checks on empty stack
    public PermitCard drawPermitCard(PermitPos pos) {
        PermitCard drawn;
        if(pos == PermitPos.RIGHT) {
            drawn = rightPermitCard;
            rightPermitCard = regionPermitCards.remove(regionPermitCards.size()-1);
        } else {
            drawn = leftPermitCard;
            leftPermitCard = regionPermitCards.remove(regionPermitCards.size()-1);
        }
        notifyObservers();
        return drawn;
    }

    public void addPermitEndOfStack(PermitCard permitCard) {
        regionPermitCards.add(0,permitCard);
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

    public Iterator<Town> townIterator() {
        return regionTowns.iterator();
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
