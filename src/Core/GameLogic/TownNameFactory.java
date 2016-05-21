package Core.GameLogic;

import Core.GameModel.TownName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * Created by Matteo on 18/05/16.
 */
public class TownNameFactory {
    public TownNameFactory() {}

    public static TownName getTownName(ArrayList<TownName> townNameList) {
        ArrayList<TownName> townNamePartialList = new ArrayList<>(Arrays.asList(TownName.values()));
        townNamePartialList.removeAll(townNameList);
        Random random = new Random();
        int townIndex = random.nextInt(townNamePartialList.size());
        return townNamePartialList.get(townIndex);
    }
}
