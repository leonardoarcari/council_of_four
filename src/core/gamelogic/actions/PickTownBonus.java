package core.gamelogic.actions;

import core.Player;
import core.gamemodel.Town;

/**
 * Created by Matteo on 25/05/16.
 */
public class PickTownBonus extends Action implements BonusAction {
    private Town town;

    public PickTownBonus(Player player, Town town) {
        super(player);
        this.town = town;
    }

    public Town getTown() {
        return town;
    }
}
