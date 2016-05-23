package Core.GameLogic.Actions;

import Core.Player;

/**
 * Created by Matteo on 23/05/16.
 */
public abstract class Action {
    private Player player;

    public Action() {
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
