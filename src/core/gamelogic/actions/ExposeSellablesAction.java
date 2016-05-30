package core.gamelogic.actions;

import core.Player;
import core.gamemodel.OnSaleItem;

import java.util.List;

/**
 * Created by Matteo on 30/05/16.
 */
public class ExposeSellablesAction extends Action implements MarketAction {
    List<OnSaleItem> onSaleItems;

    public ExposeSellablesAction(Player player, List<OnSaleItem> onSaleItems) {
        super(player);
        this.onSaleItems = onSaleItems;
    }

    public List<OnSaleItem> getOnSaleItems() {
        return onSaleItems;
    }
}
