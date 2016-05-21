package Core.GameLogic;

import Core.GameModel.RegionType;
import Core.GameModel.Town;
import Core.GameModel.TownName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * Created by Matteo on 18/05/16.
 */
public class TownNameFactory {
    public TownNameFactory() {}

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
