package client.clientconnection;

import client.CachedData;
import client.View.GUI;
import client.View.TownsWithBonusView;
import core.connection.GameBoardInterface;
import core.connection.InfoProcessor;
import core.gamelogic.actions.ChatAction;
import core.gamelogic.actions.EndTurnAction;
import core.gamelogic.actions.MarketSyncAction;
import core.gamelogic.actions.SyncAction;
import core.gamemodel.Councilor;
import core.gamemodel.WealthPath;
import core.gamemodel.modelinterface.*;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Leonardo Arcari on 03/06/2016.
 */
public class GUInfoProcessor implements InfoProcessor {
    private GUI gui;

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
            } else if (action.equals(MarketSyncAction.AUCTION_START_ACTION)) {
                gui.showBuyItemView();
            } else if (action.equals(MarketSyncAction.END_MARKET_ACTION)) {
                gui.hideMarket();
            }
        }
    }
}
