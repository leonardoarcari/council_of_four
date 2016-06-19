package core.gamemodel.modelinterface;

import core.Player;

import java.util.List;
import java.util.Map;

/**
 * Created by Leonardo Arcari on 06/06/2016.
 */
public interface AbstractPathInterface {
    List<List<Player>> getPlayers();
    int getPlayerPosition(Player player);
    Map<Integer, List<Player>> getPodium();

}
