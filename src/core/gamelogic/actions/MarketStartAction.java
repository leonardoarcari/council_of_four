package core.gamelogic.actions;

import java.io.Serializable;

/**
 * Created by Leonardo Arcari on 18/06/2016.
 */
public class MarketStartAction implements MarketAction, Serializable{
    private final int timer;

    public MarketStartAction(int timer) {
        this.timer = timer;
    }

    public int getTimer() {
        return timer;
    }
}
