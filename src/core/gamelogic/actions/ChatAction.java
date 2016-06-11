package core.gamelogic.actions;

import core.Player;

/**
 * Created by Leonardo Arcari on 11/06/2016.
 */
public class ChatAction extends Action implements SpecialAction{
    private final String message;

    public ChatAction(Player player, String message) {
        super(player);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
