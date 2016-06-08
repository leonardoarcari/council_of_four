package core.gamemodel;

import core.Observer;
import core.Player;
import core.Subject;
import core.gamelogic.AbstractBonusFactory;
import core.gamelogic.BonusFactory;
import core.gamelogic.BonusOwner;
import core.gamemodel.bonus.Bonus;
import core.gamemodel.modelinterface.RegionInterface;

import java.util.*;

/**
 * Created by Matteo on 20/05/16. Need to add LinkTown method and attributes
 */
public class Region implements RegionInterface, Subject{
    private boolean regionCardTaken;
    private RegionType regionType;
    private transient CouncilorsBalcony regionBalcony;
    private transient List<PermitCard> regionPermitCards;
    private PermitCard rightPermitCard;
    private PermitCard leftPermitCard;
    private int remainingPermits;
    private transient Map<TownName, Town> regionTowns;
    private RegionCard regionCard;

    private transient List<Observer> observers;

    public Region(RegionCard regionCard, RegionType regionType, Councilor[] councilors) {

        // Region objects initialization
        this.regionCard = regionCard;
        this.regionType = regionType;
        regionPermitCards = new Vector<>();
        regionBalcony = new CouncilorsBalcony(regionType);
        regionTowns = Collections.synchronizedMap(new HashMap<>());
        createPermitCards(regionType);
        leftPermitCard = regionPermitCards.remove(regionPermitCards.size()-1);
        rightPermitCard = regionPermitCards.remove(regionPermitCards.size()-1);
        remainingPermits = regionPermitCards.size();
        createRegionBalcony(councilors);
        setTowns();

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
                regionTowns.put(TownName.A, new Town(TownName.A, bonusFactory.createOneBonus()));
                regionTowns.put(TownName.B, new Town(TownName.B, bonusFactory.createOneBonus()));
                regionTowns.put(TownName.C, new Town(TownName.C, bonusFactory.createOneBonus()));
                regionTowns.put(TownName.D, new Town(TownName.D, bonusFactory.createOneBonus()));
                regionTowns.put(TownName.E, new Town(TownName.E, bonusFactory.createOneBonus()));
                break;
            case HILLS:
                regionTowns.put(TownName.F, new Town(TownName.F, bonusFactory.createOneBonus()));
                regionTowns.put(TownName.G, new Town(TownName.G, bonusFactory.createOneBonus()));
                regionTowns.put(TownName.H, new Town(TownName.H, bonusFactory.createOneBonus()));
                regionTowns.put(TownName.I, new Town(TownName.I, bonusFactory.createOneBonus()));
                regionTowns.put(TownName.J, new Town(TownName.J, bonusFactory.createOneBonus()));
                break;
            case MOUNTAINS:
                regionTowns.put(TownName.K, new Town(TownName.K, bonusFactory.createOneBonus()));
                regionTowns.put(TownName.L, new Town(TownName.L, bonusFactory.createOneBonus()));
                regionTowns.put(TownName.M, new Town(TownName.M, bonusFactory.createOneBonus()));
                regionTowns.put(TownName.N, new Town(TownName.N, bonusFactory.createOneBonus()));
                regionTowns.put(TownName.O, new Town(TownName.O, bonusFactory.createOneBonus()));
                break;
        }
    }

    public void buildEmporium(Player player, TownName town) {
        getTownByName(town).createEmporium(player);
    }

    public Town getTownByName(TownName townName) {
        Town t = regionTowns.get(townName);
        if (t != null) return t;
        else throw new NoSuchElementException();
    }

    public PermitCard drawPermitCard(PermitPos pos) {
        PermitCard drawn;
        if(pos == PermitPos.RIGHT) {
            drawn = rightPermitCard;
            if(regionPermitCards.size() >= 1) {
                rightPermitCard = regionPermitCards.remove(regionPermitCards.size() - 1);
            } else rightPermitCard = null;
        } else {
            drawn = leftPermitCard;
            if(regionPermitCards.size() >= 1) {
                leftPermitCard = regionPermitCards.remove(regionPermitCards.size() - 1);
            } else leftPermitCard = null;
        }
        remainingPermits = regionPermitCards.size(); //Need to update the size for the players
        notifyObservers();
        return drawn;
    }

    public PermitCard peekPermitCard(PermitPos pos) {
        if(pos == PermitPos.LEFT) return leftPermitCard;
        else return rightPermitCard;
    }

    public void addPermitEndOfStack(PermitCard permitCard) {
        regionPermitCards.add(0,permitCard);
    }

    public RegionCard drawRegionCard() throws AlreadyTakenException {
        if (!regionCardTaken) {
            regionCardTaken = true;
            notifyObservers(); // RegionCard must disappear from Region
            return regionCard;
        } else throw new AlreadyTakenException();
    }

    public boolean allTownsCaptured(Player player) {
        Iterator<Town> iterator = townIterator();
        while (iterator.hasNext()) {
            if (!iterator.next().hasEmporium(player)) return false;
        }
        return true;
    }

    public Councilor updateBalcony(Councilor councilor) {
        return regionBalcony.addCouncilor(councilor);
    }

    public List<PermitCard> getRegionPermitCards() {
        return regionPermitCards;
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
    public RegionType getRegionType() {
        return regionType;
    }

    public Iterator<Town> townIterator() {
        return regionTowns.values().iterator();
    }

    public CouncilorsBalcony getRegionBalcony() {
        return regionBalcony;
    }

    @Override
    public void registerObserver(Observer observer) {
        regionBalcony.registerObserver(observer);
        regionTowns.values().stream().forEach(town -> town.registerObserver(observer));
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        regionBalcony.removeObserver(observer);
        regionTowns.values().stream().forEach(town -> town.removeObserver(observer));
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
