package core.gamelogic.actions;

import java.io.Serializable;

/**
 * This class marks the beginning of the turn of the player,
 * setting the timer of his turn.
 */
public class YourTurnAction implements Serializable {
    // It is the turn time out
    private final int timerLength;

    /**
     * @param timerLength is the value of the time out
     */
    public YourTurnAction(int timerLength) {
        this.timerLength = timerLength;
    }

    /**
     * @return the value of the timeout
     */
    public int getTimerLength() {
        return timerLength;
    }
}
