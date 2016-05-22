package Core.GameModel;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

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
        boardBalcony = new CouncilorsBalcony();
        seaRegion = new Region(RegionCard.SEA, RegionType.SEA);
        hillsRegion = new Region(RegionCard.HILLS, RegionType.HILLS);
        mountainsRegion = new Region(RegionCard.MOUNTAINS, RegionType.MOUNTAINS);
    }

    public void createPoliticsCard() {
        for (CouncilColor color: CouncilColor.values()) {
            for (int j = 0; j < 12; j++) {
                politicsCardPool.add(new PoliticsCard(color));
            }
            if(!color.equals(CouncilColor.RAINBOW)) {
                politicsCardPool.add(new PoliticsCard(color));
            }
        }
    }

    public void shuffleDeck() {
        for(int i = 0; i < 10; i++) {
            Collections.shuffle(politicsCardPool);
        }
    }

    /**
     * No first_turn creator, the game logic will handle that
     */

    public void createCouncilors() {
        Vector<CouncilColor> coucilorsColor = new Vector<>(Arrays.asList(
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

    public void assignCouncilors() {
        fillBalcony(seaRegion);
        fillBalcony(hillsRegion);
        fillBalcony(mountainsRegion);
        for(int i = 0; i < 4; i++) {
            boardBalcony.addCouncilor(councilorPool.remove(0));
        }
    }

    private void fillBalcony(Region region) {
        Councilor[] councHelper = new Councilor[4];
        for( int i = 0; i < 4; i++) {
            councHelper[i] = councilorPool.remove(0);
        }
        region.createRegionBalcony(councHelper);
    }
}
