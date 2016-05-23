package Server;

import Core.Connection.InfoProcessor;
import Core.GameLogic.Actions.FastAction;
import Core.GameLogic.Actions.MarketAction;
import Core.GameLogic.Actions.NormalAction;
import Core.GameLogic.Actions.SyncAction;

/**
 * Created by Leonardo Arcari on 20/05/2016.
 */
public class ServerProcessor implements InfoProcessor {
    private Game game;

    public ServerProcessor(Game game) {
        this.game = game;
    }

    @Override
    public void processInfo(Object info) {
        if (info instanceof NormalAction) {
            //TODO: Add Normal Actions
        } else if (info instanceof MarketAction) {
            //TODO: Add Market actions
        } else if (info instanceof FastAction) {
            //TODO: Add Fast actions
        } else if (info instanceof SyncAction) {
            //TODO: Add Sync Action
        }
    }
}
