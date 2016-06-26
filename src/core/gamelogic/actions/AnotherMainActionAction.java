package core.gamelogic.actions;

import core.Player;

/**
 * This class represents the fast action that allows the player to do
 * another main action.
 *
 * @see FastAction
 */
public class AnotherMainActionAction extends Action implements FastAction {

    /**
     * @see Action
     */
    public AnotherMainActionAction(Player player) {
        super(player);
    }
}
