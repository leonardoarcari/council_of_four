package core.gamelogic.actions;

import core.Player;

import java.io.Serializable;

/**
 * Created by Matteo on 23/05/16.
 */
public abstract class Action implements Serializable {
    private static final long serialVersionUID = 1L;
    private final Player player;

    public Action(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

}
