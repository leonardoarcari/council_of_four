package core.gamelogic.actions;

import core.Player;

import java.io.Serializable;

/**
 * This class is the abstraction of all the possible action of the game,
 * which all share the same structure: there is always a player that makes
 * them.
 */
public abstract class Action implements Serializable {
    // Serialization constant attribute
    private static final long serialVersionUID = 1L;

    // Attribute of the class
    private final Player player;

    /**
     * @param player is the player that makes the action
     */
    public Action(Player player) {
        this.player = player;
    }

    /**
     * @return the player who makes the action
     */
    public Player getPlayer() {
        return player;
    }

}
