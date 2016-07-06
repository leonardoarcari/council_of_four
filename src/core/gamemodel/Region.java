package core.gamemodel;

import core.Observer;
import core.Player;
import core.Subject;
import core.gamelogic.AbstractBonusFactory;
import core.gamelogic.BonusFactory;
import core.gamelogic.BonusOwner;
import core.gamemodel.modelinterface.RegionInterface;

import java.io.Serializable;
import java.util.*;

/**
 * This class implements two interfaces and their relative methods. From RegionInterface
 * it inherits the getters. Some methods, such as buildEmporium, are not found in the interface: this
 * because the interface is the only object exposed when communicating with the client (and
 * referring to the Façacde Pattern this implies that the client cannot access all of this
 * class methods but only the one described in the interface) and those methods are only
 * needed on the server side, when updating the model. From the Subject interface it inherits
 * all the observer modifier methods. This interface is part of the Observer Pattern: every
 * time a subject, that is a game object or even a player, is updated, the connections of
 * each player, which are "observing" the subjects, are notified, and send the updated objects
 * to the connected clients.
 */
public class Region implements RegionInterface, Subject, Serializable{
    // Attributes of the class
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

    /**
     * The constructor creates all of the elements of the region, such
     * as towns, permit cards and the balcony
     *
     * @param regionCard is the region card contained by the region
     * @param regionType is the type of the region
     * @param councilors are the councilors of the balcony of the region
     */
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
            regionPermitCards.add(new PermitCard(region, bonusFactory, i));
        }
    }

    private void createRegionBalcony(Councilor[] councilors) {
        for (Councilor councilor : councilors) {    //Length has to be 4
            regionBalcony.addCouncilor(councilor);
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
                regionTowns.put(TownName.J, new Town(TownName.J, null));
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

    /**
     * This method proxies the creation of the emporium to the city, given its name
     *
     * @param player is the player who is building an emporium
     * @param town is the name of the city where the player wants to build the emporium in
     */
    public void buildEmporium(Player player, TownName town) {
        getTownByName(town).createEmporium(player);
    }

    /**
     * @param townName name of the town to search
     * @return the town by the name
     */
    public Town getTownByName(TownName townName) {
        Town t = regionTowns.get(townName);
        if (t != null) return t;
        else throw new NoSuchElementException();
    }

    /**
     * @param pos is the position of the card to take
     * @return the chosen permit card, removing it from the permit deck
     */
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

    /**
     * @param pos is the position of the card to take
     * @return the permit card chosen, without removing it from the permit deck
     */
    public PermitCard peekPermitCard(PermitPos pos) {
        if(pos == PermitPos.LEFT) return leftPermitCard;
        else return rightPermitCard;
    }

    /**
     * @param permitCard is the permit to add at the bottom of the permit deck
     */
    public void addPermitEndOfStack(PermitCard permitCard) {
        regionPermitCards.add(0,permitCard);
    }

    /**
     * @return the region card
     * @throws AlreadyTakenException
     */
    public RegionCard drawRegionCard() throws AlreadyTakenException {
        if (!regionCardTaken) {
            regionCardTaken = true;
            notifyObservers(); // RegionCard must disappear from Region
            return regionCard;
        } else throw new AlreadyTakenException();
    }

    /**
     * @param player is the player that must be checked
     * @return whether the player has built an emporium in all the towns of the region
     */
    public boolean allTownsCaptured(Player player) {
        Iterator<Town> iterator = townIterator();
        while (iterator.hasNext()) {
            if (!iterator.next().hasEmporium(player)) return false;
        }
        return true;
    }

    /**
     * This method proxies the insertion of a councilor to the balcony of the region
     *
     * @param councilor is the councilor to add to the balcony
     * @return the councilor that left the councilor
     */
    public Councilor updateBalcony(Councilor councilor) {
        return regionBalcony.addCouncilor(councilor);
    }

    /**
     * @return whether the region card is already taken or not
     */
    @Override
    public boolean isRegionCardTaken() {
        return regionCardTaken;
    }

    /**
     * @return the left exposed permit card
     */
    @Override
    public PermitCard getLeftPermitCard() {
        return leftPermitCard;
    }

    /**
     * @return the right exposed permit card
     */
    @Override
    public PermitCard getRightPermitCard() {
        return rightPermitCard;
    }

    /**
     * @return the region card
     */
    @Override
    public RegionCard getRegionCard() {
        if (!regionCardTaken) {
            return regionCard;
        } else return RegionCard.NULL;
    }

    /**
     * @return the region type
     */
    @Override
    public RegionType getRegionType() {
        return regionType;
    }

    /**
     * @return the iterator of the towns inside the region
     */
    public Iterator<Town> townIterator() {
        return regionTowns.values().iterator();
    }

    /**
     * @return the balcony of the region
     */
    public CouncilorsBalcony getRegionBalcony() {
        return regionBalcony;
    }

    /**
     * @see Subject
     */
    @Override
    public void registerObserver(Observer observer) {
        regionBalcony.registerObserver(observer);
        regionTowns.values().stream().forEach(town -> town.registerObserver(observer));
        observers.add(observer);
    }

    /**
     * @see Subject
     */
    @Override
    public void removeObserver(Observer observer) {
        regionBalcony.removeObserver(observer);
        regionTowns.values().stream().forEach(town -> town.removeObserver(observer));
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
     * This enumeration is used to distinguish the exposed permit cards
     * of the region: left or right, while the others are covered
     */
    public enum PermitPos {
        LEFT, RIGHT
    }

    /**
     * Inner class that represents the exception when trying to take
     * the region card when it is already taken
     */
    private class AlreadyTakenException extends RuntimeException {
        public AlreadyTakenException() {
            super();
        }
    }
}
