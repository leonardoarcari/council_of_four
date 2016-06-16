package client.View.cli;

import client.CachedData;
import client.View.UserInterface;
import core.connection.GameBoardInterface;
import core.gamelogic.actions.ChatAction;
import core.gamemodel.Councilor;
import core.gamemodel.RegionType;
import core.gamemodel.modelinterface.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leonardo Arcari on 16/06/2016.
 */
public class CLI implements UserInterface {
    private final CLIState electCouncilorMainState;
    private final CLIState electCouncilorFastState;
    private final CLIState buildEmporiumState;
    private final CLIState buildEmporiumWithKingState;
    private final CLIState buyPermitCardState;
    private final CLIState doOtherActionState;
    private final CLIState hireServantState;
    private final CLIState changePermitState;
    private final CLIState mainState;
    private final CLIState mainActionState;
    private final CLIState fastActionState;
    private final CLIState marketAuctionState;
    private final CLIState marketExposureState;
    private final CLIState objectStatusState;
    private final CLIState pickTownBonusState;
    private final CLIState permitAgainState;
    private final CLIState pickPermitState;
    private final CLIState playerState;
    private final CLIState waitingState;

    private CLIState currentState;
    private CLIState nextState;
    private BufferedReader in;

    public CLI() {
        electCouncilorMainState = new ElectCouncilorState(ElectCouncilorState.Type.MAIN_ACTION, this);
        electCouncilorFastState = new ElectCouncilorState(ElectCouncilorState.Type.FAST_ACTION, this);
        buildEmporiumState = new BuildEmporiumState(this);
        buildEmporiumWithKingState = new BuildEmpoWithKingState(this);
        buyPermitCardState = new BuyPermitCardState(this);
        doOtherActionState = new DoOtherActionState(this);
        hireServantState = new HireServantState(this);
        changePermitState = new ChangePermitState(this);
        mainState = new MainState(this);
        mainActionState = new MainActionState(this);
        fastActionState = new FastActionState(this);
        marketAuctionState = new MarketAuctionState(this);
        marketExposureState = new MarketExposureState(this);
        objectStatusState = new ObjectStatusState(this);
        pickTownBonusState = new PickTownBonusState(this);
        permitAgainState = new PermitAgainState(this);
        pickPermitState = new PickPermitState(this);
        playerState = new PlayerState(this);
        waitingState = new WaitingState(this);

        in = new BufferedReader(new InputStreamReader(System.in));
    }

    public void run() {
        while (true) {
            try {
                currentState.showMenu();
                boolean correct = false;
                while (!correct) {
                    String input = in.readLine();
                    if(nextState.equals(currentState))
                        try {
                            currentState.readInput(input);
                            correct = true;
                        } catch (IllegalArgumentException e) {
                            correct = false;
                        }
                    else {
                        System.out.println("-- Not valid input: state has changed --");
                        currentState = nextState;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public CLIState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(CLIState currentState) {
        this.currentState = currentState;
    }

    public CLIState getElectCouncilorMainState() {
        return electCouncilorMainState;
    }

    public CLIState getElectCouncilorFastState() {
        return electCouncilorFastState;
    }

    public CLIState getBuildEmporiumState() {
        return buildEmporiumState;
    }

    public CLIState getBuildEmporiumWithKingState() {
        return buildEmporiumWithKingState;
    }

    public CLIState getBuyPermitCardState() {
        return buyPermitCardState;
    }

    public CLIState getDoOtherActionState() {
        return doOtherActionState;
    }

    public CLIState getHireServantState() {
        return hireServantState;
    }

    public CLIState getChangePermitState() {
        return changePermitState;
    }

    public CLIState getMainState() {
        return mainState;
    }

    public CLIState getMainActionState() {
        return mainActionState;
    }

    public CLIState getFastActionState() {
        return fastActionState;
    }

    public CLIState getObjectStatusState() {
        return objectStatusState;
    }

    public CLIState getPickTownBonusState() {
        return pickTownBonusState;
    }

    public CLIState getPlayerState() {
        return playerState;
    }

    public CLIState getWaitingState() {
        return waitingState;
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
        nextState = marketExposureState;
    }

    @Override
    public void showBuyItemView() {
        nextState = marketAuctionState;
    }

    @Override
    public void hideMarket() {
        nextState = waitingState;
    }

    @Override
    public void updatePlayer(PlayerInterface player) {
        CachedData.getInstance().setMe(player);
    }

    @Override
    public void startGame() {
        currentState = nextState = mainState;
    }

    @Override
    public void pickTownBonus() {
        nextState = pickTownBonusState;
    }

    @Override
    public void appendChatMessage(ChatAction action) {
        //Do nothing. Chat is disabled in CLI
    }

    @Override
    public void yourTurn() {
        setFastActionAvailable(true);
        setMainActionAvailable(true);
        nextState = mainState;
    }

    @Override
    public void endTurn() {
        setMainActionAvailable(false);
        setFastActionAvailable(false);
        currentState.invalidateState();
        nextState = mainState;
    }

    @Override
    public void setMainActionAvailable(boolean mainActionAvailable) {
        CachedData.getInstance().setMainActionAvailable(mainActionAvailable);
        nextState = mainState;
    }

    @Override
    public void setFastActionAvailable(boolean fastActionAvailable) {
        CachedData.getInstance().setFastActionAvailable(fastActionAvailable);
        nextState = mainState;
    }

    @Override
    public void showRedeemPermitView() {
        nextState = permitAgainState;
    }

    @Override
    public void showDrawFreePermitView() {
        nextState = pickPermitState;
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
        currentState.invalidateState();
        nextState = waitingState;
    }

    @Override
    public void forceBuyingEnd() {
        currentState.invalidateState();
        nextState = waitingState;
    }
}
