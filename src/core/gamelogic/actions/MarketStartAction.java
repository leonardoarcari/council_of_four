package core.gamelogic.actions;

import java.io.Serializable;

/**
 * This class represents the action that starts the market exposure phase of the market.
 *
 * @see MarketAction
 */
public class MarketStartAction implements MarketAction, Serializable{
    // Is the timer given to the exposure phase
    private final int timer;

    /**
     * @param timer is the fixed timeout for the exposure phase
     */
    public MarketStartAction(int timer) {
        this.timer = timer;
    }

    /**
     * @return the set timer for the auction phase
     */
    public int getTimer() {
        return timer;
    }
}
