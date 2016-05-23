package Core.GameLogic.Actions;

import Core.Player;

/**
 * Created by Leonardo Arcari on 23/05/2016.
 */
public class EndTurnAction extends Action implements SyncAction {
    public EndTurnAction(Player player) {
        super(player);
    }
}
