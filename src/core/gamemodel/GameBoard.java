package core.gamemodel;

import core.gamelogic.AbstractBonusFactory;
import core.gamelogic.BonusFactory;
import core.gamelogic.BonusOwner;
import core.gamemodel.bonus.Bonus;
import core.Player;
import core.Observer;
import core.Subject;
import server.serverconnection.ServerConnection;

import java.util.*;

/**
 * Created by Matteo on 21/05/16.
 */
public class GameBoard implements Subject{
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

    public static GameBoard createGameBoard(List<Player> players) {
        GameBoard gameBoard = new GameBoard();
        Iterator<Player> iterator = players.iterator();
        int wealthPos = 10;
        while (iterator.hasNext()) {
            Player player = iterator.next();
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
    public void discardCard(PoliticsCard card) {
        discardedCards.add(card);
    }

    public void electCouncilor(Councilor councilor, RegionType regionType) {
        Councilor toAddCounc;

        if (regionType.equals(RegionType.KINGBOARD)) {
            toAddCounc = boardBalcony.addCouncilor(councilor);
        } else {
            Region region = getRegionBy(regionType);
            toAddCounc = region.updateBalcony(councilor);
        }

        councilorPool.remove(councilor);
        councilorPool.add(toAddCounc);
        notifyObservers();
    }

    public PermitCard drawPermitCard(RegionType type, Region.PermitPos pos) {
        Region region = getRegionBy(type);
        return region.drawPermitCard(pos);
    }

    public void buildEmporium(Player player,RegionType type, TownName townName) {
        getRegionBy(type).buildEmporium(player, townName);
    }

    public void moveKing(TownName startingTown, TownName buildingTown) {
        regionFromTownName(startingTown).getTownByName(startingTown).setKing(false);
        regionFromTownName(buildingTown).getTownByName(buildingTown).setKing(true);
    }

    public PoliticsCard drawPoliticsCard() {
        return politicsCardPool.pop();
    }

    public List<Servant> hireServants(int number) {
        List<Servant> servants = new ArrayList<>();
        for(int i = 0; i < number; i++) {
            servants.add(servantPool.remove(0));
        }
        return servants;
    }

    public void returnServant(Servant servant) {
        servantPool.add(servant);
    }

    public List<Bonus> moveNobilityPath(Player player, int increment) {
        nobilityPath.movePlayer(player, increment);
        return nobilityPath.retrieveBonus(player);
    }

    public void moveWealthPath (Player player, int increment) {
        wealthPath.movePlayer(player, increment);
    }

    public void moveVictoryPath(Player player, int increment) {
        victoryPath.movePlayer(player, increment);
    }

    public Map<TownName, Town> getTownsMap() {
        Map<TownName,Town> townMap = new HashMap<>();

        fillTownMapBy(seaRegion, townMap);
        fillTownMapBy(hillsRegion, townMap);
        fillTownMapBy(mountainsRegion, townMap);
        return townMap;
    }

    public void fillTownMapBy(Region region, Map<TownName,Town> townMap) {
        Iterator<Town> iterator = region.townIterator();
        while(iterator.hasNext()){
            Town town = iterator.next();
            townMap.put(town.getTownName(),town);
        }
    }

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

    public boolean checkRegionCompletion(Player player,RegionType regionType) {
        Region buildingRegion = getRegionBy(regionType);
        return buildingRegion.allTownsCaptured(player);
    }

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
    public Region getRegionBy(RegionType regionType) {
        if(regionType.equals(RegionType.SEA)) return seaRegion;
        else if (regionType.equals(RegionType.HILLS)) return hillsRegion;
        else return mountainsRegion;
    }

    public Region regionFromTownName(TownName townName) {
        if(townName.ordinal() < 5) {
            return seaRegion;
        } else if (townName.ordinal() < 10) {
            return hillsRegion;
        } else {
            return mountainsRegion;
        }
    }

    private Iterator<Region> regionIterator() {
        return Arrays.asList(seaRegion, hillsRegion, mountainsRegion).iterator();
    }

    private Iterator<TownTypeCard> townTypeCardIterator() {
        return townTypeCards.iterator();
    }

    public Iterator<Councilor> councilorIterator() { return councilorPool.iterator(); }

    public boolean checkRoyalSize() {
        if(royalCardPool.size() == 0) return false;
        else return true;
    }

    public RoyalCard popRoyalCard() {
        return royalCardPool.pop();
    }

    public NobilityPath getNobilityPath() {
        return nobilityPath;
    }

    public WealthPath getWealthPath() {
        return wealthPath;
    }

    public VictoryPath getVictoryPath() {
        return victoryPath;
    }

    public Showcase getShowcase() { return showcase; }

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

    @Override
    public void notifyObservers() {
        for (Observer o : observers) {
            o.update(this);
        }
    }

    public void notifyChildren() {
        notifyObservers();
        boardBalcony.notifyObservers();
        seaRegion.notifyObservers();
        hillsRegion.notifyObservers();
        mountainsRegion.notifyObservers();
        nobilityPath.notifyObservers();
        victoryPath.notifyObservers();
        wealthPath.notifyObservers();
    }
}