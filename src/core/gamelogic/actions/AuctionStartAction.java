package core.gamelogic.actions;

import java.io.Serializable;

/**
 * This class represents the action that starts the auction phase of the market.
 *
 * @see MarketAction
 */
public class AuctionStartAction implements MarketAction, Serializable {
    // Is the timer given to the auction phase
    private final int timer;

    /**
     * @param timer is the fixed timeout for the auction phase
     */
    public AuctionStartAction(int timer) {
        this.timer = timer;
    }

    /**
     * @return the set timer for the auction phase
     */
    public int getTimer() {
        return timer;
    }
}
