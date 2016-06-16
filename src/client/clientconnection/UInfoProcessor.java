package client.clientconnection;

import client.CachedData;
import client.View.UserInterface;
import core.connection.GameBoardInterface;
import core.connection.InfoProcessor;
import core.gamelogic.actions.*;
import core.gamemodel.WealthPath;
import core.gamemodel.modelinterface.*;

import java.util.concurrent.ScheduledExecutorService;


/**
 * Created by Leonardo Arcari on 03/06/2016.
 */
public class UInfoProcessor implements InfoProcessor {
    private UserInterface userInterface;

    public UInfoProcessor(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

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
            } else if (action.equals(SyncAction.YOUR_TURN)) {
                userInterface.yourTurn();
                ScheduledExecutorService executor = CachedData.getInstance().getExecutor();
                //executor.scheduleAtFixedRate(() -> gui.setTimer(setElapsedTurnTime()),2,1, TimeUnit.SECONDS);
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
        } else if (info.getClass().equals(EndTurnAction.class)) {
            userInterface.endTurn();
        } else if (info.getClass().equals(MarketSyncAction.class)) {
            MarketSyncAction action = (MarketSyncAction) info;
            if (action.equals(MarketSyncAction.MARKET_START_ACTION)) {
                userInterface.showExposeView();
                ScheduledExecutorService executor = CachedData.getInstance().getExecutor();
                //executor.scheduleAtFixedRate(() -> setElapsedExposureTime(true),2,1, TimeUnit.SECONDS);
            } else if (action.equals(MarketSyncAction.AUCTION_START_ACTION)) {
                userInterface.showBuyItemView();
                ScheduledExecutorService executor = CachedData.getInstance().getExecutor();
                //executor.scheduleAtFixedRate(() -> setElapsedExposureTime(false),2,1, TimeUnit.SECONDS);
            } else if (action.equals(MarketSyncAction.END_MARKET_ACTION)) {
                userInterface.hideMarket();
            }
        }
    }

    private String setElapsedTurnTime() {
        int time = CachedData.getInstance().isNormalTimerExpired();
        if(time == -1) return "Waiting for next turn..";
        else return "Remaining time: " + time + " seconds";
    }

    private void setElapsedExposureTime(boolean exposure) {
        int time = CachedData.getInstance().isMarketPhaseEnded(exposure);
        if(time == -1) {
            if(exposure) userInterface.forceExposureEnd();
            else userInterface.forceBuyingEnd();
            return;
        }
    }
}
