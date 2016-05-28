package core.gamelogic.actions;

import core.Player;
import core.gamemodel.TownName;

/**
 * Created by Matteo on 25/05/16.
 */
public class PickTownBonusAction extends Action implements SpecialAction {
    private TownName townName;

    public PickTownBonusAction(Player player, TownName townName) {
        super(player);
        this.townName = townName;
    }

    public TownName getTownName() {
        return townName;
    }
}
