package core.gamelogic.actions;

import java.io.Serializable;

/**
 * Created by Leonardo Arcari on 18/06/2016.
 */
public class AuctionStartAction implements MarketAction, Serializable {
    private final int timer;

    public AuctionStartAction(int timer) {
        this.timer = timer;
    }

    public int getTimer() {
        return timer;
    }
}
