package client.View;

import client.ControllerUI;
import core.gamemodel.modelinterface.GameBoardInterface;
import core.gamelogic.actions.ChatAction;
import core.gamelogic.actions.PodiumAction;
import core.gamemodel.modelinterface.*;

/**
 * A <code>UserInterface</code> is responsible to show to the client's user the state of the game, letting him/her to
 * interact with it. The following methods are common to a user interface of Council of Four that reacts to server's
 * messages and new model states, updating accordingly.
 */
public interface UserInterface {

    void updateBalcony(BalconyInterface balcony);

    void updateNobilityPath(NobilityPathInterface nobility);

    void updateWealthPath(WealthPathInterface wealthPath);

    void updateVictoryPath(VictoryPathInterface victory);

    void updateTown(TownInterface town);

    void updatePermitCard(RegionInterface region);

    void updateGameBoardData(GameBoardInterface gameBoardInterface);

    void updateShowCase(ShowcaseInterface showcase);

    void showExposeView();

    void showBuyItemView();

    void hideMarket();

    void updatePlayer(PlayerInterface player);

    void startGame();

    void endGame(PodiumAction podiumAction);

    void pickTownBonus();

    void appendChatMessage(ChatAction action);

    void yourTurn();

    void endTurn();

    void setMainActionAvailable(boolean mainActionAvailable);

    void setFastActionAvailable(boolean fastActionAvailable);

    void showRedeemPermitView();

    void showDrawFreePermitView();

    void setTimer(int time);

    void forceExposureEnd();

    void forceBuyingEnd();

    void loadMap(String fileName);

    void handleServerMessage(String message);

    ControllerUI getController();
}
