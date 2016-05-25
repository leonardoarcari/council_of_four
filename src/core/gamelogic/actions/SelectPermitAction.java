package core.gamelogic.actions;

import core.Player;
import core.gamemodel.PermitCard;
import core.gamemodel.Region;

/**
 * Created by Matteo on 25/05/16.
 */
public class SelectPermitAction extends Action implements BonusAction{
    private Region region;
    private PermitCard permitCard;

    public SelectPermitAction(Player player, Region region, PermitCard permitCard) {
        super(player);
        this.region = region;
        this.permitCard = permitCard;
    }

    public Region getRegion() {
        return region;
    }

    public PermitCard getPermitCard() {
        return permitCard;
    }
}
