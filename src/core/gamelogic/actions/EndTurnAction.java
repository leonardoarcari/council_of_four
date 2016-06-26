package core.gamelogic.actions;

import core.Player;

/**
 * This class acts as a marker, informing the player that is turn has ended.
 *
 * @see Action
 */
public class EndTurnAction extends Action {
    /**
     * @param player is the player whose turn has to end
     */
    public EndTurnAction(Player player) {
        super(player);
    }
}
