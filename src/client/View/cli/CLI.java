package client.View.cli;

import client.CachedData;
import client.ControllerUI;
import client.View.UserInterface;
import core.Player;
import core.connection.GameBoardInterface;
import core.gamelogic.actions.ChatAction;
import core.gamelogic.actions.PlayerInfoAction;
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
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";

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
    private final CLIState loginState;

    private CLIState currentState;
    private boolean validState;
    private BufferedReader in;

    private String username;
    private String nickname;

    private ControllerUI controller;

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
        loginState = new LoginState(this);

        in = new BufferedReader(new InputStreamReader(System.in));
        controller = new ControllerUI(this);
        CachedData.getInstance().setController(controller);
        currentState = loginState;
        validState = true;
    }

    public void run() {
        while (true) {
            try {
                currentState.showMenu();
                boolean correct = false;
                while (!correct) {
                    String input = in.readLine();
                    if (validState)
                        try {
                            currentState.readInput(input);
                            correct = true;
                        } catch (IllegalArgumentException e) {
                            correct = false;
                        }
                    else {
                        validState = true;
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setValidation(boolean validation) {
        validState = validation;
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

    public ControllerUI getController() {
        return controller;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
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
        currentState = marketExposureState;
        validState = false;
        System.out.println(ANSI_RED + "Press any key + return to expose items" + ANSI_RESET);
    }

    @Override
    public void showBuyItemView() {
        currentState = marketAuctionState;
        validState = false;
        System.out.println(ANSI_RED + "Press any key + return to participate the auction" + ANSI_RESET);
    }

    @Override
    public void hideMarket() {
        currentState = waitingState;
        validState = false;
        System.out.println(ANSI_RED + "Press any key + return to continue" + ANSI_RESET);
    }

    @Override
    public void updatePlayer(PlayerInterface player) {
        CachedData.getInstance().setMe(player);
    }

    @Override
    public void startGame() {
        currentState = mainState;
        validState = false;
        controller.sendInfo(new PlayerInfoAction(
                (Player) CachedData.getInstance().getMe(),
                username,
                nickname));
    }

    @Override
    public void pickTownBonus() {
        currentState = pickTownBonusState;
        validState = false;
        System.out.println(ANSI_RED + "Press any key + return to redeem a town bonus" + ANSI_RESET);
    }

    @Override
    public void appendChatMessage(ChatAction action) {
        //Do nothing. Chat is disabled in CLI
    }

    @Override
    public void handleServerMessage(String message) {
        System.out.println("Sistema Informativo says: " + message);
    }

    @Override
    public void loadMap(String fileName) {
        //Do nothing. No need to show anything in CLI
    }

    @Override
    public void yourTurn() {
        CachedData.getInstance().myTurnProperty().setValue(true);
        CachedData.getInstance().mainActionAvailableProperty().setValue(true);
        CachedData.getInstance().fastActionAvailableProperty().setValue(true);
        currentState = mainState;
        validState = false;
        System.out.println(ANSI_RED + "Your turn, press any key + return to continue" + ANSI_RESET);
    }

    @Override
    public void endTurn() {
        System.out.println(ANSI_RED + "Turn ended! Press any key + return to continue" + ANSI_RESET);
        CachedData.getInstance().setMainActionAvailable(false);
        CachedData.getInstance().setFastActionAvailable(false);
        CachedData.getInstance().setMyTurn(false);
        currentState.invalidateState();
        currentState = mainState;
        validState = false;
    }

    @Override
    public void setMainActionAvailable(boolean mainActionAvailable) {
        CachedData.getInstance().setMainActionAvailable(mainActionAvailable);
        currentState = mainState;
        validState = false;
        System.out.println(ANSI_RED + "Press any key + return to continue"+ ANSI_RESET);
    }

    @Override
    public void setFastActionAvailable(boolean fastActionAvailable) {
        CachedData.getInstance().setFastActionAvailable(fastActionAvailable);
        currentState = mainState;
        validState = false;
        System.out.println(ANSI_RED + "Press any key + return to continue" + ANSI_RESET);
    }

    @Override
    public void showRedeemPermitView() {
        currentState = permitAgainState;
        validState = false;
        System.out.println(ANSI_RED + "Press any key + return to redeem a permit" + ANSI_RESET);
    }

    @Override
    public void showDrawFreePermitView() {
        currentState = pickPermitState;
    }

    @Override
    public void setTimer(String text) {
        int timer;
        try {
            timer = Integer.valueOf(text);
        } catch (NumberFormatException e) {
            return;
        }
        if (timer < 5) System.out.println(ANSI_RED + "Hurry! Time over in " + timer + " seconds" + ANSI_RESET);
        else if (timer % 5 == 0) System.out.println(ANSI_RED + "Time over in " + timer + " seconds" + ANSI_RESET);
    }

    @Override
    public void forceExposureEnd() {
        currentState.invalidateState();
        validState = false;
        currentState = waitingState;
        System.out.println(ANSI_RED + "Exposure time expired" + ANSI_RESET);
    }

    @Override
    public void forceBuyingEnd() {
        currentState.invalidateState();
        validState = false;
        System.out.println(ANSI_RED + "Auction time expired" + ANSI_RESET);
    }
}
