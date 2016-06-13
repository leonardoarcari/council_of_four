package client.clientconnection;

import client.CachedData;
import client.View.ExposeSellableView;
import client.View.GUI;
import client.View.TownsWithBonusView;
import core.connection.GameBoardInterface;
import core.connection.InfoProcessor;
import core.gamelogic.actions.*;
import core.gamemodel.Councilor;
import core.gamemodel.VictoryPath;
import core.gamemodel.WealthPath;
import core.gamemodel.modelinterface.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * Created by Leonardo Arcari on 03/06/2016.
 */
public class GUInfoProcessor implements InfoProcessor {
    private GUI gui;
    private static int elapsed = 20;

    public GUInfoProcessor(GUI gui) {
        this.gui = gui;
    }

    @Override
    public synchronized void processInfo(Object info) {
        if (info instanceof TownInterface) {
            TownInterface town = (TownInterface) info;
            CachedData.getInstance().putTown(town.getTownName(), town);
            gui.getTownView(town.getTownName()).update(town);
            gui.populateTownBonus(town);
        } else if (info instanceof BalconyInterface) {
            BalconyInterface balcony = (BalconyInterface) info;
            CachedData.getInstance().putBalcony(balcony.getRegion(), balcony);
            gui.updateBalcony((BalconyInterface) info);
        } else if (info instanceof WealthPathInterface) {
            gui.updateWealthPath((WealthPath) info);
        } else if (info instanceof RegionInterface) {
            gui.updatePermitCard((RegionInterface) info);
            gui.updateRegionBonus((RegionInterface) info);
        } else if (info instanceof NobilityPathInterface) {
            gui.updateNobilityPath((NobilityPathInterface) info);
        } else if (info instanceof VictoryPathInterface) {
            gui.updateVictoryPath((VictoryPathInterface) info);
        } else if (info instanceof GameBoardInterface) {
            GameBoardInterface gameboard = (GameBoardInterface) info;
            List<Councilor> councilorPool = new ArrayList<>();
            gameboard.councilorIterator().forEachRemaining(councilorPool::add);
            CachedData.getInstance().setCouncilorPool(councilorPool);
            gui.updateGameBoardData((GameBoardInterface) info);
        } else if (info instanceof PlayerInterface) {
            CachedData.getInstance().setMe((PlayerInterface) info);
            gui.updatePlayer((PlayerInterface) info);
        } else if (info instanceof ShowcaseInterface) {
            gui.updateShowCase((ShowcaseInterface) info);
        } else if (info.getClass().equals(ChatAction.class)) {
            gui.appendChatMessage((ChatAction) info);
        } else if (info instanceof SyncAction) {
            SyncAction action = (SyncAction) info;
            if (action.equals(SyncAction.GAME_START)) {
                gui.startGame();
            } else if (action.equals(SyncAction.YOUR_TURN)) {
                gui.yourTurn();
                ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
                //executor.scheduleAtFixedRate(() -> gui.setTimer(setElapsedTurnTime(executor)),2,1, TimeUnit.SECONDS);
            } else if (action.equals(SyncAction.PICK_PERMIT_AGAIN)) {
                gui.showRedeemPermitView();
            } else if (action.equals((SyncAction.PICK_TOWN_BONUS))) {
                TownsWithBonusView.getInstance().changeBonusListener();
            } else if (action.equals(SyncAction.MAIN_ACTION_DONE)) {
                gui.setMainActionAvailable(false);
            } else if (action.equals(SyncAction.MAIN_ACTION_AGAIN)) {
                gui.setMainActionAvailable(true);
            } else if (action.equals(SyncAction.FAST_ACTION_DONE)) {
                gui.setFastActionAvailable(false);
            }
        } else if (info.getClass().equals(EndTurnAction.class)) {
            gui.endTurn();
        } else if (info.getClass().equals(MarketSyncAction.class)) {
            MarketSyncAction action = (MarketSyncAction) info;
            if (action.equals(MarketSyncAction.MARKET_START_ACTION)) {
                gui.showExposeView();
                ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
                executor.scheduleAtFixedRate(() -> setElapsedExposureTime(executor, true),2,1, TimeUnit.SECONDS);
            } else if (action.equals(MarketSyncAction.AUCTION_START_ACTION)) {
                gui.showBuyItemView();
                ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
                executor.scheduleAtFixedRate(() -> setElapsedExposureTime(executor, false),2,1, TimeUnit.SECONDS);
            } else if (action.equals(MarketSyncAction.END_MARKET_ACTION)) {
                gui.hideMarket();
            }
        }
    }

    private final String setElapsedTurnTime(ScheduledExecutorService executor) {
        if(elapsed == 15) {
            executor.shutdownNow();
            elapsed = 20;
            gui.forceEnd();
            return "Turn ended!";
        }
        elapsed--;
        return "Remaining time: " + elapsed + " seconds";
    }

    private final void setElapsedExposureTime(ScheduledExecutorService executor, boolean exposure) {
        if(elapsed == 15) {
            executor.shutdownNow();
            elapsed = 20;
            if(exposure) gui.forceExposureEnd();
            else gui.forceBuyingEnd();
            return;
        }
        elapsed--;
    }
}
