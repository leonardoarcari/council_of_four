package core.gamelogic.actions;

import java.io.Serializable;

/**
 * Created by leonardoarcari on 17/06/16.
 */
public class YourTurnAction implements Serializable {
    private final int timerLength;

    public YourTurnAction(int timerLength) {
        this.timerLength = timerLength;
    }

    public int getTimerLength() {
        return timerLength;
    }
}
