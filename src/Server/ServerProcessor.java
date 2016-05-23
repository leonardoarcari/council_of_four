package Server;

import Core.Connection.InfoProcessor;
import Core.GameLogic.Actions.*;
import Core.GameModel.Councilor;
import Core.GameModel.RegionType;
import Core.Player;

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
            if(info.getClass().equals(CouncilorElectionAction.class)){
                councilorElection((CouncilorElectionAction) info);
            }
        } else if (info instanceof MarketAction) {
            //TODO: Add Market actions
        } else if (info instanceof FastAction) {
            //TODO: Add Fast actions
        } else if (info instanceof SyncAction) {
            //TODO: Add Sync Action
        }
    }

    private void councilorElection(CouncilorElectionAction action) {
        Player player = action.getPlayer();
        Councilor councilor = action.getNewCouncilor();
        RegionType regionType = action.getRegionType();
        game.getGameBoard().electCouncilor(councilor, regionType);
        game.getGameBoard().updateWealthPath(player, 4);
    }
}
