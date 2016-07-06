package client.clientconnection;

import client.View.UserInterface;
import core.gamemodel.modelinterface.GameBoardInterface;
import core.connection.InfoProcessor;
import core.gamelogic.actions.*;
import core.gamemodel.WealthPath;
import core.gamemodel.modelinterface.*;


/**
 * A <code>UIInfoProcessor</code> is a <code>InfoProcessor</code> implementation on the client to handle the server's
 * messages processing to apply their informative content on the user interface. This concerns the game's model objects
 * being sent to update the user interface accordingly to their new state, or synchronization messages to inform the
 * client on some game event.
 */
public class UInfoProcessor implements InfoProcessor {
    private UserInterface userInterface;

    /**
     * Initializes a <code>UInfoProcessor</code> to process server's messages and applying them on the <code>userInterface
     * </code> chosen by the user
     * @param userInterface userInterface implementation to update accordingly to server's messages
     */
    public UInfoProcessor(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    /**
     * Updates the <code>UserInterface. Info</code> is processed depending on its dynamic type mostly delegating the task
     * to <code>UserInterface</code>'s methods.
     * @param info Message/Information to handle
     */
    @Override
    public synchronized void processInfo(Object info) {
        if (info instanceof TownInterface) {
            userInterface.updateTown((TownInterface) info);
        } else if (info instanceof BalconyInterface) {
            userInterface.updateBalcony((BalconyInterface) info);
        } else if (info instanceof WealthPathInterface) {
            userInterface.updateWealthPath((WealthPath) info);
        } else if (info instanceof RegionInterface) {
            userInterface.updatePermitCard((RegionInterface) info);
        } else if (info instanceof NobilityPathInterface) {
            userInterface.updateNobilityPath((NobilityPathInterface) info);
        } else if (info instanceof VictoryPathInterface) {
            userInterface.updateVictoryPath((VictoryPathInterface) info);
        } else if (info instanceof GameBoardInterface) {
            userInterface.updateGameBoardData((GameBoardInterface) info);
        } else if (info instanceof PlayerInterface) {
            userInterface.updatePlayer((PlayerInterface) info);
        } else if (info instanceof ShowcaseInterface) {
            userInterface.updateShowCase((ShowcaseInterface) info);
        } else if (info.getClass().equals(ChatAction.class)) {
            userInterface.appendChatMessage((ChatAction) info);
        } else if (info instanceof SyncAction) {
            SyncAction action = (SyncAction) info;
            if (action.equals(SyncAction.GAME_START)) {
                userInterface.startGame();
            } else if (action.equals(SyncAction.PICK_PERMIT_AGAIN)) {
                userInterface.showRedeemPermitView();
            } else if (action.equals(SyncAction.DRAW_PERMIT_BONUS)) {
                userInterface.showDrawFreePermitView();
            } else if (action.equals((SyncAction.PICK_TOWN_BONUS))) {
                userInterface.pickTownBonus();
            } else if (action.equals(SyncAction.MAIN_ACTION_DONE)) {
                userInterface.setMainActionAvailable(false);
            } else if (action.equals(SyncAction.MAIN_ACTION_AGAIN)) {
                userInterface.setMainActionAvailable(true);
            } else if (action.equals(SyncAction.FAST_ACTION_DONE)) {
                userInterface.setFastActionAvailable(false);
            }
        } else if (info.getClass().equals(YourTurnAction.class)) {
            userInterface.yourTurn();
            userInterface.getController().runTurnTimer(((YourTurnAction) info).getTimerLength());
        } else if (info.getClass().equals(EndTurnAction.class)) {
            userInterface.endTurn();
        } else if (info.getClass().equals(LoadMapAction.class)) {
            userInterface.loadMap(((LoadMapAction) info).getMapName());
        }  else if (info.getClass().equals(ServerMessage.class)) {
            userInterface.handleServerMessage(((ServerMessage) info).getMessage());
        } else if (info instanceof MarketAction) {
            if (info.getClass().equals(MarketStartAction.class)) {
                userInterface.showExposeView();
                userInterface.getController().runMarketTimer(((MarketStartAction) info).getTimer(), true);
            } else if (info.getClass().equals(AuctionStartAction.class)) {
                userInterface.showBuyItemView();
                userInterface.getController().runMarketTimer(((AuctionStartAction) info).getTimer(), false);
            }
        } else if( info.getClass().equals(PodiumAction.class)) {
            userInterface.endGame((PodiumAction) info);
        } else if (info.getClass().equals(MarketSyncAction.class)) {
            MarketSyncAction action = (MarketSyncAction) info;
            if (action.equals(MarketSyncAction.END_MARKET_ACTION)) {
                userInterface.hideMarket();
            }
        }
    }
}
