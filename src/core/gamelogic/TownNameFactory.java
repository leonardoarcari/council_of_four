package core.gamelogic;

import core.gamemodel.RegionType;
import core.gamemodel.TownName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * This class acts as a factory of town names for the permit cards.
 */
public class TownNameFactory {
    public TownNameFactory() {}

    /**
     * This method generates a random town name for a card, but it doesn't act
     * all alone. To work, it needs to know the already extracted town names of
     * the card, so that there isn't any duplicate. To accomplish so the permit
     * gives the method the list of town names already set and it's region type.
     * This way, the method filters the data to work on and increases its
     * efficiency.
     *
     * @param townNameList is the list of the already generated town names
     * @param region is the type of the region where the permit card that
     *               is requesting the town names is
     * @return a randomly generated town name, not duplicated and in the specific
     * region requested
     */
    public static TownName getTownName(ArrayList<TownName> townNameList, RegionType region) {
        ArrayList<TownName> townNameByRegion;
        switch(region) {
            case SEA:
                townNameByRegion = new ArrayList<>(Arrays.asList(
                        TownName.A,
                        TownName.B,
                        TownName.C,
                        TownName.D,
                        TownName.E));
                break;
            case HILLS:
                townNameByRegion = new ArrayList<>(Arrays.asList(
                        TownName.F,
                        TownName.G,
                        TownName.H,
                        TownName.I,
                        TownName.J));
                break;
            case MOUNTAINS:
                townNameByRegion = new ArrayList<>(Arrays.asList(
                        TownName.K,
                        TownName.L,
                        TownName.M,
                        TownName.N,
                        TownName.O
                ));
                break;
            default:
                townNameByRegion = new ArrayList<>();
                break;
        }
        townNameByRegion.removeAll(townNameList);
        Random random = new Random();
        int townIndex = random.nextInt(townNameByRegion.size());
        return townNameByRegion.get(townIndex);
    }
}
