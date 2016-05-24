package Core.GameModel;

import Core.GameModel.Bonus.Bonus;
import Core.Player;
import Server.Observer;
import Server.Subject;

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
    private CouncilorsBalcony boardBalcony;
    private transient Region seaRegion;
    private transient Region hillsRegion;
    private transient Region mountainsRegion;
    private transient NobilityPath nobilityPath;
    private transient WealthPath wealthPath;

    private transient List<Server.Observer> observers;

    public GameBoard() {
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


        boardBalcony = new CouncilorsBalcony();
        Arrays.stream(fillBalcony()).forEach(councilor -> boardBalcony.addCouncilor(councilor));
        nobilityPath = new NobilityPath();
        wealthPath = new WealthPath();
    }

    private void createCouncilors() {
        ArrayList<CouncilColor> coucilorsColor = new ArrayList<>(Arrays.asList(
                CouncilColor.BLACK,
                CouncilColor.CYAN,
                CouncilColor.ORANGE,
                CouncilColor.PINK,
                CouncilColor.PURPLE,
                CouncilColor.WHITE
        ));
        int id = 0;
        for(CouncilColor color: coucilorsColor) {
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

    public void setNobilityPlayer(Player player) {
        nobilityPath.setPlayer(player);
    }

    public void discardCard(PoliticsCard card) {
        discardedCards.add(card);
    }

    public void electCouncilor(Councilor councilor, RegionType regionType) {
        Councilor toAddCounc;

        if (regionType.equals(RegionType.KINGBOARD)) {
            toAddCounc = boardBalcony.addCouncilor(councilor);
        } else {
            Region region;
            switch (regionType) {
                case SEA:
                    region = seaRegion;
                    break;
                case HILLS:
                    region = hillsRegion;
                    break;
                case MOUNTAINS:
                    region = mountainsRegion;
                    break;
                default:    // Never reached
                    region = null;
                    break;
            }
            toAddCounc = region.updateBalcony(councilor);
        }
        councilorPool.add(toAddCounc);
        notifyObservers();
    }

    public PermitCard drawPermitCard(RegionType type, Region.PermitPos pos) {
        switch (type) {
            case SEA:
                return seaRegion.drawPermitCard(pos);
            case HILLS:
                return hillsRegion.drawPermitCard(pos);
            case MOUNTAINS:
                return mountainsRegion.drawPermitCard(pos);
            default: // Shouldn't happen
                return null;
        }
    }

    public void moveWealthPath (Player player, int increment) {
        wealthPath.movePlayer(player, increment);
    }

    public void updateTown(Player player, Town town) {
        town.createEmporium(player);
    }

    public void discartCard(PoliticsCard politicCard) {
        discardedCards.add(politicCard);
    }

    //TODO: Check for empty deck
    public PoliticsCard drawPoliticsCard() {
        return politicsCardPool.pop();
    }

    public Servant hireServant() {
        return servantPool.remove(0);
    }

    public List<Bonus> moveNobilityPath(Player player, int increment) {
        nobilityPath.movePlayer(player, increment);
        return nobilityPath.retrieveBonus(player);
    }

    @Override
    public void registerObserver(Server.Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Server.Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer o : observers) {
            o.update(this);
        }
    }
}