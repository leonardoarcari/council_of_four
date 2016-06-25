package core.gamemodel;

import core.Observer;
import core.Player;
import core.Subject;
import core.gamemodel.modelinterface.GameBoardInterface;
import core.gamelogic.AbstractBonusFactory;
import core.gamelogic.BonusFactory;
import core.gamelogic.BonusOwner;
import core.gamemodel.bonus.Bonus;
import server.serverconnection.ServerConnection;

import java.io.Serializable;
import java.util.*;

/**
 * This class implements two interfaces and their relative methods. From GameBoardInterface
 * it inherits some getters. Almost all public methods doesn't derive from the interface: this
 * because the interface is the only object exposed when communicating with the client (and
 * referring to the Façacde Pattern this implies that the client cannot access all of this
 * class methods but only the one described in the interface) and those methods are only
 * needed on the server side, when updating the model. From the Subject interface it inherits
 * all the observer modifier methods. This interface is part of the Observer Pattern: every
 * time a subject, that is a game object or even a player, is updated, the connections of
 * each player, which are "observing" the subjects, are notified, and send the updated objects
 * to the connected clients.
 */
public class GameBoard implements Subject, GameBoardInterface, Serializable{
    // Attributes of the class
    private transient RegionType boardType;
    private transient Stack<PoliticsCard> politicsCardPool;
    private transient List<PoliticsCard> discardedCards;

    private Stack<RoyalCard> royalCardPool;
    private List<Councilor> councilorPool;
    private List<TownTypeCard> townTypeCards;
    private transient List<Servant> servantPool;
    private transient CouncilorsBalcony boardBalcony;
    private transient Region seaRegion;
    private transient Region hillsRegion;
    private transient Region mountainsRegion;
    private transient NobilityPath nobilityPath;
    private transient WealthPath wealthPath;
    private transient VictoryPath victoryPath;
    private transient Showcase showcase;

    private transient List<Observer> observers;

    /**
     * This static method is called to create all the game board objects in
     * the correct order. First the game board skeleton is created, then all
     * the final settings are done, such as the removal of the servants and
     * politics card for all the players. Before returning, the method registers
     * the observers, delegating each object of his children observers registration.
     *
     * @param players are the players of the game
     * @return the game board fully created
     */
    public static GameBoard createGameBoard(List<Player> players) {
        GameBoard gameBoard = new GameBoard();
        Iterator<Player> iterator = players.iterator();
        int wealthPos = 10;
        int servantsNumber = 1;
        while (iterator.hasNext()) {
            Player player = iterator.next();
            player.hireServants(gameBoard.hireServants(servantsNumber++));
            for(int i = 0; i < 6; i++)
                player.addPoliticsCard(gameBoard.drawPoliticsCard());
            gameBoard.nobilityPath.setPlayer(player);
            gameBoard.wealthPath.setPlayer(player, wealthPos++);
            gameBoard.victoryPath.setPlayer(player);
            gameBoard.showcase.setPlayers(player);
            gameBoard.registerObserver((ServerConnection) player.getConnection());
            player.registerObserver((ServerConnection) player.getConnection());
        }
        return gameBoard;
    }

    private GameBoard() {
        boardType = RegionType.KINGBOARD;
        politicsCardPool = new Stack<>();
        discardedCards = new Vector<>();
        royalCardPool = new Stack<>();
        councilorPool = new Vector<>();
        townTypeCards = new Vector<>();
        servantPool = new Vector<>();
        observers = new Vector<>();

        // Pools creation
        createCouncilors();
        shufflePool(councilorPool);
        createPoliticsCard();
        shufflePool(politicsCardPool);
        createServantsPool();
        Arrays.stream(RoyalCard.values()).forEach(royalCard -> royalCardPool.push(royalCard));
        Arrays.stream(TownTypeCard.values()).forEach(townTypeCard -> townTypeCards.add(townTypeCard));

        //Regions creation
        seaRegion = new Region(RegionCard.SEA, RegionType.SEA, fillBalcony());
        hillsRegion = new Region(RegionCard.HILLS, RegionType.HILLS, fillBalcony());
        mountainsRegion = new Region(RegionCard.MOUNTAINS, RegionType.MOUNTAINS, fillBalcony());


        boardBalcony = new CouncilorsBalcony(RegionType.KINGBOARD);
        Arrays.stream(fillBalcony()).forEach(councilor -> boardBalcony.addCouncilor(councilor));
        nobilityPath = new NobilityPath(nobilityBonus());
        wealthPath = new WealthPath();
        victoryPath = new VictoryPath();
        showcase = new Showcase();
    }

    /* Creation methods */
    private void createCouncilors() {
        ArrayList<CouncilColor> councilorsColor = new ArrayList<>(Arrays.asList(
                CouncilColor.BLACK,
                CouncilColor.CYAN,
                CouncilColor.ORANGE,
                CouncilColor.PINK,
                CouncilColor.PURPLE,
                CouncilColor.WHITE));
        int id = 0;
        for(CouncilColor color: councilorsColor) {
            for(int i = 0; i < 4; i++) {
                councilorPool.add(new Councilor(color, id++));
            }
        }
    }

    private void createPoliticsCard() {
        for (CouncilColor color: CouncilColor.values()) {
            for (int j = 0; j < 12; j++) {
                politicsCardPool.add(new PoliticsCard(color));
            }
            if(!color.equals(CouncilColor.RAINBOW)) {
                politicsCardPool.add(new PoliticsCard(color));
            }
        }
    }

    private void shufflePool(List<?> pool) {
        for(int i = 0; i < 10; i++) {
            Collections.shuffle(pool);
        }
    }

    private void createServantsPool() {
        for(int i = 0; i < 48; i++) {
            servantPool.add(new Servant());
        }
    }

    private Councilor[] fillBalcony() {
        Councilor[] councHelper = new Councilor[4];
        for( int i = 0; i < 4; i++) {
            councHelper[i] = councilorPool.remove(0);
        }
        return councHelper;
    }

    private List<List<Bonus>> nobilityBonus() {
        AbstractBonusFactory bonusFactory = BonusFactory.getFactory(BonusOwner.NOBILITY);
        List<List<Bonus>> bonusPath = new ArrayList<>(21);
        bonusPath.add(new ArrayList<>());
        for (int i = 1; i < 21; i++) {
            bonusPath.add(bonusFactory.generateBonuses());
        }
        return bonusPath;
    }

    /* Logic handler methods */

    /**
     * @param card is the politics card to discard and add to the discarded deck
     *             of the game board
     */
    public void discardCard(PoliticsCard card) {
        discardedCards.add(card);
    }

    /**
     * This method is called whenever the CouncilorElection action is done by a
     * player
     *
     * @param councilor is the councilor to elect
     * @param regionType is the type of the region whose balcony will change,
     *                   due to the insertion of the councilor
     */
    public void electCouncilor(Councilor councilor, RegionType regionType) {
        Councilor toAddCounc;

        if (regionType.equals(RegionType.KINGBOARD)) {
            toAddCounc = boardBalcony.addCouncilor(councilor);
        } else {
            Region region = getRegionBy(regionType);
            toAddCounc = region.updateBalcony(councilor);
        }

        System.out.println(councilorPool.remove(councilor));
        councilorPool.add(toAddCounc);
        notifyObservers();
    }

    /**
     * This method is called whenever the BuyPermitCard action is done by a
     * player
     *
     * @param type is the type of the region owning the card
     * @param pos is the position, left or right, of the card to draw
     * @return the drawn permit card, if possible
     */
    public PermitCard drawPermitCard(RegionType type, Region.PermitPos pos) {
        Region region = getRegionBy(type);
        return region.drawPermitCard(pos);
    }

    /**
     * This method is called whenever a player does an action that allows him
     * to build an emporium
     *
     * @param player is the player building an emporium
     * @param type is the type of the region where the player is building
     * @param townName is the name of the town where the player is building
     */
    public void buildEmporium(Player player,RegionType type, TownName townName) {
        getRegionBy(type).buildEmporium(player, townName);
    }

    /**
     * @param startingTown is the town where the king was set before an action
     * @param buildingTown is the town where the king is after an action
     */
    public void moveKing(TownName startingTown, TownName buildingTown) {
        regionFromTownName(startingTown).getTownByName(startingTown).setKing(false);
        regionFromTownName(buildingTown).getTownByName(buildingTown).setKing(true);
    }

    /**
     * Every turn, or when a bonus of drawing cards occurs, this method is
     * invoked and pops the card from the politics deck
     *
     * @return the top politics card of the deck
     */
    public PoliticsCard drawPoliticsCard() {
        return politicsCardPool.pop();
    }

    /**
     * @param number is the number of hired servants
     * @return the hired servants
     */
    public List<Servant> hireServants(int number) {
        List<Servant> servants = new ArrayList<>();
        for(int i = 0; i < number; i++) {
            servants.add(servantPool.remove(0));
        }
        return servants;
    }

    /**
     * @param servant is the servant to give back to the game board servant pool
     */
    public void returnServant(Servant servant) {
        servantPool.add(servant);
    }

    /**
     * This method is called whenever a player moves on the nobility path
     *
     * @param player is the player that is moving on the path
     * @param increment is the value of steps the player can proceed
     * @return the list of bonuses gained during the player "walk"
     */
    public List<Bonus> moveNobilityPath(Player player, int increment) {
        int positionBefore = nobilityPath.getPlayerPosition(player);
        nobilityPath.movePlayer(player, increment);
        return nobilityPath.retrieveBonus(player, positionBefore);
    }

    /**
     * This method is called whenever a player moves on the wealth path
     *
     * @param player is the player that is moving on the path
     * @param increment is how much the player has gained or spent for some action
     */
    public void moveWealthPath (Player player, int increment) {
        wealthPath.movePlayer(player, increment);
    }

    /**
     * This method is called whenever a player moves on the victory path
     *
     * @param player is the player that is moving on the path
     * @param increment is the value of steps the player can proceed
     */
    public void moveVictoryPath(Player player, int increment) {
        victoryPath.movePlayer(player, increment);
    }

    /**
     * @return a map of the town objects and their name
     */
    public Map<TownName, Town> getTownsMap() {
        Map<TownName,Town> townMap = new HashMap<>();

        fillTownMapBy(seaRegion, townMap);
        fillTownMapBy(hillsRegion, townMap);
        fillTownMapBy(mountainsRegion, townMap);
        return townMap;
    }

    private void fillTownMapBy(Region region, Map<TownName,Town> townMap) {
        Iterator<Town> iterator = region.townIterator();
        while(iterator.hasNext()){
            Town town = iterator.next();
            townMap.put(town.getTownName(),town);
        }
    }

    /**
     * This method is called whenever a player builds an emporium, to check if he can
     * gain a town type bonus card, having built in every town of the same type
     *
     * @param player is the player who has built an emporium
     * @param townName name of the town where the player has just built an emporium
     * @return whether the player has built an emporium in all the towns of
     * the same type
     */
    public boolean checkTypeCompletion(Player player, TownName townName) {
        Map<TownName,Town> map = getTownsMap();
        TownType type = map.get(townName).getTownType();
        for(TownName name : map.keySet()){
            if(map.get(name).getTownType().equals(type)) {
                if(!map.get(name).hasEmporium(player)) return false;
            }
        }
        return true;
    }

    /**
     * This method is called whenever a player builds an emporium, to check if
     * he can gain a region bonus card, having built in every town of a region
     *
     * @param player is the player who has built an emporium
     * @param regionType is the region where the player has just built an emporium
     * @return whether the player has built an emporium in all the towns of a region
     */
    public boolean checkRegionCompletion(Player player,RegionType regionType) {
        Region buildingRegion = getRegionBy(regionType);
        return buildingRegion.allTownsCaptured(player);
    }

    /**
     * This method is called whenever the checkTypeCompletion returns true
     *
     * @param townName is the name of the last emporium has just built an emporium
     * @return the town type bonus card with the same type of the town with the given name
     */
    public TownTypeCard acquireTownTypeCard(TownName townName) {
        Map<TownName,Town> map = getTownsMap();
        TownType type = map.get(townName).getTownType();
        Iterator<TownTypeCard> iterator = townTypeCardIterator();
        while(iterator.hasNext()) {
            TownTypeCard typeCard = iterator.next();
            if(typeCard.getTownType().equals(type)) {
                return typeCard;
            }
        }
        throw new NoSuchElementException();
    }

    /* Utility methods */

    /**
     * @param regionType is the type of the region
     * @return a region, given its type
     */
    public Region getRegionBy(RegionType regionType) {
        if(regionType.equals(RegionType.SEA)) return seaRegion;
        else if (regionType.equals(RegionType.HILLS)) return hillsRegion;
        else return mountainsRegion;
    }

    /**
     * @param townName is the name of a town
     * @return the region containing a town with the given name
     */
    public Region regionFromTownName(TownName townName) {
        if(townName.ordinal() < 5) {
            return seaRegion;
        } else if (townName.ordinal() < 10) {
            return hillsRegion;
        } else {
            return mountainsRegion;
        }
    }

    /**
     * @see GameBoardInterface
     */
    @Override
    public Iterator<RoyalCard> royalCardIterator() {
        return royalCardPool.iterator();
    }

    /**
     * @see GameBoardInterface
     */
    @Override
    public Iterator<TownTypeCard> townTypeCardIterator() {
        return townTypeCards.iterator();
    }

    /**
     * @see GameBoardInterface
     */
    @Override
    public Iterator<Councilor> councilorIterator() { return councilorPool.iterator(); }

    /**
     * @return whether there are more royal cards or not
     */
    public boolean checkRoyalSize() {
        return royalCardPool.size() != 0;
    }

    /**
     * @return the top royal card of the royal deck
     */
    public RoyalCard popRoyalCard() {
        return royalCardPool.pop();
    }

    /**
     * @return the nobility path
     */
    public NobilityPath getNobilityPath() {
        return nobilityPath;
    }

    /**
     * @return the wealth path
     */
    public WealthPath getWealthPath() {
        return wealthPath;
    }

    /**
     * @return the victory path
     */
    public VictoryPath getVictoryPath() {
        return victoryPath;
    }

    /**
     * @return the showcase
     */
    public Showcase getShowcase() { return showcase; }

    /**
     * @see Subject
     */
    @Override
    public void registerObserver(Observer observer) {
        boardBalcony.registerObserver(observer);
        seaRegion.registerObserver(observer);
        hillsRegion.registerObserver(observer);
        mountainsRegion.registerObserver(observer);
        nobilityPath.registerObserver(observer);
        victoryPath.registerObserver(observer);
        wealthPath.registerObserver(observer);
        showcase.registerObserver(observer);
        observers.add(observer);
    }

    /**
     * @see Subject
     */
    @Override
    public void removeObserver(Observer observer) {
        boardBalcony.removeObserver(observer);
        seaRegion.removeObserver(observer);
        hillsRegion.removeObserver(observer);
        mountainsRegion.removeObserver(observer);
        nobilityPath.removeObserver(observer);
        victoryPath.removeObserver(observer);
        wealthPath.removeObserver(observer);
        showcase.removeObserver(observer);
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
     * This method forces the dispatch of the game objects
     * when the game board is created, informing the players
     * of the real state of the game and its objects.
     */
    public void notifyChildren() {
        notifyObservers();
        boardBalcony.notifyObservers();

        seaRegion.notifyObservers();
        seaRegion.getRegionBalcony().notifyObservers();
        hillsRegion.notifyObservers();
        hillsRegion.getRegionBalcony().notifyObservers();
        mountainsRegion.notifyObservers();
        mountainsRegion.getRegionBalcony().notifyObservers();

        getTownsMap().values().forEach(Town::notifyObservers);

        nobilityPath.notifyObservers();
        victoryPath.notifyObservers();
        wealthPath.notifyObservers();
        showcase.notifyObservers();
    }
}