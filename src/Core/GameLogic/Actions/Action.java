package core.gamelogic.actions;

import core.Player;

/**
 * Created by Matteo on 23/05/16.
 */
public abstract class Action {
    private final Player player;

    public Action(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

}
