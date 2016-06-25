package client.View.cli;

import client.CachedData;
import client.ControllerUI;
import client.View.UserInterface;
import core.Player;
import core.gamemodel.modelinterface.GameBoardInterface;
import core.gamelogic.actions.ChatAction;
import core.gamelogic.actions.PlayerInfoAction;
import core.gamelogic.actions.PodiumAction;
import core.gamemodel.Councilor;
import core.gamemodel.RegionType;
import core.gamemodel.modelinterface.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The class is the context where the player can do choices about the game. The class
 * has a reference of every possible state of the game and dynamically changes the
 * current state. The player will be shown various menus and he will be able to insert
 * an input - and so, to do an action -.
 * The class and all the State classes have been implemented following the State Pattern:
 * the CLI represents the context and the CLIState references it contains are the States
 * of the pattern. Each time the player makes a choice, the context current state changes,
 * and different options are shown.
 */
public class CLI implements UserInterface {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";

    // States references
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
    private final CLIState podiumState;

    /* Attributes referring to the current state and its validity,
       comprehending also the input objects and the game controller
     */
    private CLIState currentState;
    private boolean validState;
    private BufferedReader in;
    private ControllerUI controller;

    // Player names info's
    private String username;
    private String nickname;

    /**
     * The constructor of the class creates every possible state and sets up the game controller
     * and the beginning state, which will be the login state.
     */
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
        waitingState = new WaitingState();
        loginState = new LoginState(this);
        podiumState = new PodiumState();

        in = new BufferedReader(new InputStreamReader(System.in));
        controller = new ControllerUI(this);
        CachedData.getInstance().setController(controller);
        currentState = loginState;
        validState = true;
    }

    /**
     * This always running method consists of two phases. First, it shows the current state
     * menu and then asks the player to insert an input. This input will be considerate
     * valid only if the validState boolean attribute, representing a change of states,
     * is true. Otherwise, the input is discarded and the new current state menu is printed,
     * starting the show/ask cycle once more.
     */
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

    /**
     * @param validation refers to a change of state
     */
    public void setValidation(boolean validation) {
        validState = validation;
    }


    /**
     * Method invoked every time the player inserts an input, thus changing the
     * state of the game.
     *
     * @param currentState refers to the actual state of the CLI flux
     */
    public void setCurrentState(CLIState currentState) {
        this.currentState = currentState;
    }

    /**
     * @return the ElectCouncilorMainState
     */
    public CLIState getElectCouncilorMainState() {
        return electCouncilorMainState;
    }

    /**
     * @return the ElectCouncilorFastState
     */
    public CLIState getElectCouncilorFastState() {
        return electCouncilorFastState;
    }

    /**
     * @return the BuildEmporiumState
     */
    public CLIState getBuildEmporiumState() {
        return buildEmporiumState;
    }

    /**
     * @return the BuildEmporiumWithKingState
     */
    public CLIState getBuildEmporiumWithKingState() {
        return buildEmporiumWithKingState;
    }

    /**
     * @return the BuyPermitCardState
     */
    public CLIState getBuyPermitCardState() {
        return buyPermitCardState;
    }

    /**
     * @return the DoOtherActionState
     */
    public CLIState getDoOtherActionState() {
        return doOtherActionState;
    }

    /**
     * @return the HireServantState
     */
    public CLIState getHireServantState() {
        return hireServantState;
    }

    /**
     * @return the ChangePermitState
     */
    public CLIState getChangePermitState() {
        return changePermitState;
    }

    /**
     * @return the MainState
     */
    public CLIState getMainState() {
        return mainState;
    }

    /**
     * @return the MainActionState
     */
    public CLIState getMainActionState() {
        return mainActionState;
    }

    /**
     * @return the FastActionState
     */
    public CLIState getFastActionState() {
        return fastActionState;
    }

    /**
     * @return the ObjectStatusState
     */
    public CLIState getObjectStatusState() {
        return objectStatusState;
    }

    /**
     * @return the PlayerState
     */
    public CLIState getPlayerState() {
        return playerState;
    }

    /**
     * @return the WaitingState
     */
    public CLIState getWaitingState() {
        return waitingState;
    }

    /**
     * @return the game controller
     */
    @Override
    public ControllerUI getController() {
        return controller;
    }

    /**
     * @param username is the username chosen by the player
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @param nickname is the nickname chosen by the player
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /************ UserInterface Methods ************/

    /**
     * @see UserInterface
     */
    @Override
    public void updateBalcony(BalconyInterface balcony) {
        CachedData.getInstance().putBalcony(balcony.getRegion(), balcony);
    }

    /**
     * @see UserInterface
     */
    @Override
    public void updateNobilityPath(NobilityPathInterface nobility) {
        CachedData.getInstance().setNobilityPath(nobility);
    }

    /**
     * @see UserInterface
     */
    @Override
    public void updateWealthPath(WealthPathInterface wealthPath) {
        CachedData.getInstance().setWealthPath(wealthPath);
    }

    /**
     * @see UserInterface
     */
    @Override
    public void updateVictoryPath(VictoryPathInterface victory) {
        CachedData.getInstance().setVictoryPath(victory);
    }

    /**
     * @see UserInterface
     */
    @Override
    public void updateTown(TownInterface town) {
        CachedData.getInstance().putTown(town.getTownName(), town);
    }

    /**
     * @see UserInterface
     */
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

    /**
     * @see UserInterface
     */
    @Override
    public void updateGameBoardData(GameBoardInterface gameBoardInterface) {
        List<Councilor> councilorPool = new ArrayList<>();
        gameBoardInterface.councilorIterator().forEachRemaining(councilorPool::add);
        CachedData.getInstance().setCouncilorPool(councilorPool);
    }

    /**
     * @see UserInterface
     */
    @Override
    public void updateShowCase(ShowcaseInterface showcase) {
        CachedData.getInstance().setShowcase(showcase);
    }

    /**
     * @see UserInterface
     */
    @Override
    public void showExposeView() {
        currentState = marketExposureState;
        validState = false;
        System.out.println(ANSI_RED + "Press any key + return to expose items" + ANSI_RESET);
    }

    /**
     * @see UserInterface
     */
    @Override
    public void showBuyItemView() {
        currentState = marketAuctionState;
        validState = false;
        System.out.println(ANSI_RED + "Press any key + return to participate the auction" + ANSI_RESET);
    }

    /**
     * @see UserInterface
     */
    @Override
    public void hideMarket() {
        currentState = waitingState;
        validState = false;
        System.out.println(ANSI_RED + "Press any key + return to continue" + ANSI_RESET);
    }

    /**
     * @see UserInterface
     */
    @Override
    public void updatePlayer(PlayerInterface player) {
        CachedData.getInstance().setMe(player);
    }

    /**
     * @see UserInterface
     */
    @Override
    public void startGame() {
        currentState = mainState;
        validState = false;
        controller.sendInfo(new PlayerInfoAction(
                (Player) CachedData.getInstance().getMe(),
                username,
                nickname));
    }

    /**
     * @see UserInterface
     */
    @Override
    public void pickTownBonus() {
        currentState = pickTownBonusState;
        validState = false;
        System.out.println(ANSI_RED + "Press any key + return to redeem a town bonus" + ANSI_RESET);
    }

    /**
     * This method does nothing. The chat is disabled in the command line interface.
     *
     * @param action is the chat action
     */
    @Override
    public void appendChatMessage(ChatAction action) {
        //Do nothing. Chat is disabled in CLI
    }

    /**
     * @see UserInterface
     */
    @Override
    public void handleServerMessage(String message) {
        System.out.println("Sistema Informativo says: " + message);
    }

    /**
     * This method does nothing. There is no need to load a map with the CLI
     *
     * @param fileName the URL of the map to load
     */
    @Override
    public void loadMap(String fileName) {
        //Do nothing. No need to show anything in CLI
    }

    /**
     * This method sets the new turn logic, allowing the player action, main and fast,
     * and changing the state to the main one, where the player can select either to see
     * the game board status, his infos or to do an action.
     *
     * @see MainState
     */
    @Override
    public void yourTurn() {
        CachedData.getInstance().myTurnProperty().setValue(true);
        CachedData.getInstance().mainActionAvailableProperty().setValue(true);
        CachedData.getInstance().fastActionAvailableProperty().setValue(true);
        currentState = mainState;
        validState = false;
        System.out.println(ANSI_RED + "Your turn, press any key + return to continue" + ANSI_RESET);
    }

    /**
     * This method sets the end turn logic, resetting the possible actions of the player
     * and changing the current state to the idle one.
     *
     * @see MainState
     */
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

    /**
     * This method sets the end game logic, changing the current state to the podium one.
     *
     * @param podiumAction is the action referring to the podium creation
     * @see PodiumState
     */
    @Override
    public void endGame(PodiumAction podiumAction) {
        Map<Integer, List<Player>> podiumMap = new HashMap<>();
        podiumMap.put(1,podiumAction.getFirst());
        podiumMap.put(2,podiumAction.getSecond());
        ((PodiumState) podiumState).setUpPodium(podiumMap);
        currentState = podiumState;
        validState = false;
        System.out.println(ANSI_RED + "Press any key + return to see the podium" + ANSI_RESET);
    }

    /**
     * @param mainActionAvailable points out whether the player has more main actions available
     *                            or not
     */
    @Override
    public void setMainActionAvailable(boolean mainActionAvailable) {
        CachedData.getInstance().setMainActionAvailable(mainActionAvailable);
        currentState = mainState;
        validState = false;
        System.out.println(ANSI_RED + "Press any key + return to continue"+ ANSI_RESET);
    }

    /**
     * @param fastActionAvailable points out whether the player has more fast actions available
     *                            or not
     */
    @Override
    public void setFastActionAvailable(boolean fastActionAvailable) {
        CachedData.getInstance().setFastActionAvailable(fastActionAvailable);
        currentState = mainState;
        validState = false;
        System.out.println(ANSI_RED + "Press any key + return to continue" + ANSI_RESET);
    }

    /**
     * @see UserInterface
     */
    @Override
    public void showRedeemPermitView() {
        currentState = permitAgainState;
        validState = false;
        System.out.println(ANSI_RED + "Press any key + return to redeem a permit" + ANSI_RESET);
    }

    /**
     * @see UserInterface
     */
    @Override
    public void showDrawFreePermitView() {
        currentState = pickPermitState;
    }

    /**
     * This method informs the player, with a red message, of the remaining time of his turn, usually
     * every ten seconds, every second when 5 seconds are missing.
     *
     * @see UserInterface
     */
    @Override
    public void setTimer(int time) {
        if (time < 5) System.out.println(ANSI_RED + "Hurry! Time over in " + time + " seconds" + ANSI_RESET);
        else if (time % 10 == 0) System.out.println(ANSI_RED + "Time over in " + time + " seconds" + ANSI_RESET);
    }

    /**
     * @see UserInterface
     */
    @Override
    public void forceExposureEnd() {
        currentState.invalidateState();
        validState = false;
        currentState = waitingState;
        System.out.println(ANSI_RED + "Exposure time expired" + ANSI_RESET);
    }

    /**
     * @see UserInterface
     */
    @Override
    public void forceBuyingEnd() {
        currentState.invalidateState();
        validState = false;
        System.out.println(ANSI_RED + "Auction time expired" + ANSI_RESET);
    }
}
