package core.gamelogic.actions;

import core.Player;

/**
 * This class represents the fast action that allows the player to
 * hire a servant from the game board pool.
 */
public class HireServantAction extends Action implements FastAction{
    /**
     * @param player is the player doing the action
     */
    public HireServantAction(Player player) {
        super(player);
    }
}
