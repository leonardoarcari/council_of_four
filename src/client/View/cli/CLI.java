package client.View.cli;

import client.CachedData;
import client.View.UserInterface;
import core.connection.GameBoardInterface;
import core.gamelogic.actions.ChatAction;
import core.gamemodel.Councilor;
import core.gamemodel.RegionType;
import core.gamemodel.modelinterface.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Leonardo Arcari on 16/06/2016.
 */
public class CLI implements UserInterface {
    Map<String, CLIState> stateMap;

    public CLI() {

    }

    /************ UserInterface Methods ************/

    @Override
    public void updateBalcony(BalconyInterface balcony) {
        CachedData.getInstance().putBalcony(balcony.getRegion(), balcony);
    }

    @Override
    public void updateNobilityPath(NobilityPathInterface nobility) {
        CachedData.getInstance().setNobilityPath(nobility);
    }

    @Override
    public void updateWealthPath(WealthPathInterface wealthPath) {
        CachedData.getInstance().setWealthPath(wealthPath);
    }

    @Override
    public void updateVictoryPath(VictoryPathInterface victory) {
        CachedData.getInstance().setVictoryPath(victory);
    }

    @Override
    public void updateTown(TownInterface town) {
        CachedData.getInstance().putTown(town.getTownName(), town);
    }

    @Override
    public void updatePermitCard(RegionInterface region) {
        if (region.getRegionType().equals(RegionType.SEA)) {
            CachedData.getInstance().setSeaRegion(region);
        } else if (region.getRegionType().equals(RegionType.HILLS)) {
            CachedData.getInstance().setHillsRegion(region);
        } else if (region.getRegionType().equals(RegionType.MOUNTAINS)) {
            CachedData.getInstance().setMountainsRegion(region);
        }
    }

    @Override
    public void updateGameBoardData(GameBoardInterface gameBoardInterface) {
        List<Councilor> councilorPool = new ArrayList<>();
        gameBoardInterface.councilorIterator().forEachRemaining(councilorPool::add);
        CachedData.getInstance().setCouncilorPool(councilorPool);
    }

    @Override
    public void updateShowCase(ShowcaseInterface showcase) {
        CachedData.getInstance().setShowcase(showcase);
    }

    @Override
    public void showExposeView() {
        //TODO: Force switching state
    }

    @Override
    public void showBuyItemView() {
        //TODO: Force switching state
    }

    @Override
    public void hideMarket() {
        //TODO: Force switching state
    }

    @Override
    public void updatePlayer(PlayerInterface player) {
        CachedData.getInstance().setMe(player);
    }

    @Override
    public void startGame() {
        //TODO: Force switching state
    }

    @Override
    public void pickTownBonus() {
        //TODO: Force switching state
    }

    @Override
    public void appendChatMessage(ChatAction action) {
        //Do nothing. Chat is disabled in CLI
    }

    @Override
    public void yourTurn() {
        //TODO: Force switching state
    }

    @Override
    public void endTurn() {
        //TODO: Force switching state
    }

    @Override
    public void setMainActionAvailable(boolean mainActionAvailable) {
        CachedData.getInstance().setMainActionAvailable(mainActionAvailable);
    }

    @Override
    public void setFastActionAvailable(boolean fastActionAvailable) {
        CachedData.getInstance().setFastActionAvailable(fastActionAvailable);
    }

    @Override
    public void showRedeemPermitView() {
        //TODO: Force switching state
    }

    @Override
    public void setTimer(String text) {
        int timer;
        try {
            timer = Integer.valueOf(text);
        } catch (NumberFormatException e) {
            return;
        }
        if (timer < 5) System.out.println("Hurry! Time over in " + timer + " seconds");
        else if (timer % 5 == 0) System.out.println("Time over in " + timer + " seconds");
    }

    @Override
    public void forceExposureEnd() {
        //TODO: Force switching state
    }

    @Override
    public void forceBuyingEnd() {
        //TODO: Force switching state
    }
}
