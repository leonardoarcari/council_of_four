package core.gamelogic.actions;

import core.Player;

/**
 * This class represents the special action that allows the player to
 * communicate with the other players using a chat.
 *
 * @see Action
 * @see SpecialAction
 */
public class ChatAction extends Action implements SpecialAction{
    // Attribute of the action
    private final String message;

    /**
     * @param player is the player who sent the message
     * @param message is the message to communicate to the other players
     */
    public ChatAction(Player player, String message) {
        super(player);
        this.message = message;
    }

    /**
     * @return the message the player wants to communicate
     */
    public String getMessage() {
        return message;
    }
}
