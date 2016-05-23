package Core.GameModel;

import java.util.*;

/**
 * Created by Matteo on 21/05/16.
 */
public class GameBoard {
    private List<PoliticsCard> politicsCardPool;
    private List<RoyalCard> royalCardPool;
    private List<Councilor> councilorPool;
    private List<TownTypeCard> townTypeCards;
    private List<Servant> servantPool;
    private CouncilorsBalcony boardBalcony;
    private Region seaRegion;
    private Region hillsRegion;
    private Region mountainsRegion;

    public GameBoard() {
        politicsCardPool = new Vector<>();
        royalCardPool = new Vector<>();
        councilorPool = new Vector<>();
        townTypeCards = new Vector<>();
        servantPool = new Vector<>();

        // Pools creation
        createCouncilors();
        shufflePool(councilorPool);
        createPoliticsCard();
        shufflePool(politicsCardPool);
        createServantsPool();

        //Regions creation
        seaRegion = new Region(RegionCard.SEA, RegionType.SEA, fillBalcony());
        hillsRegion = new Region(RegionCard.HILLS, RegionType.HILLS, fillBalcony());
        mountainsRegion = new Region(RegionCard.MOUNTAINS, RegionType.MOUNTAINS, fillBalcony());


        boardBalcony = new CouncilorsBalcony();
        Arrays.stream(fillBalcony()).forEach(councilor -> boardBalcony.addCouncilor(councilor));
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
        for(CouncilColor color: coucilorsColor) {
            for(int i = 0; i < 4; i++) {
                councilorPool.add(new Councilor(color));
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

    private void assignCouncilors() {
        for(int i = 0; i < 4; i++) {
            boardBalcony.addCouncilor(councilorPool.remove(0));
        }
    }

    private Councilor[] fillBalcony() {
        Councilor[] councHelper = new Councilor[4];
        for( int i = 0; i < 4; i++) {
            councHelper[i] = councilorPool.remove(0);
        }
        return councHelper;
    }
}
