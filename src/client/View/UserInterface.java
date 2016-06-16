package client.View;

import core.connection.GameBoardInterface;
import core.gamelogic.actions.ChatAction;
import core.gamemodel.modelinterface.*;

/**
 * Created by Leonardo Arcari on 16/06/2016.
 */
public interface UserInterface {
    //Update methods
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

    void pickTownBonus();

    void appendChatMessage(ChatAction action);

    void yourTurn();

    void endTurn();

    void setMainActionAvailable(boolean mainActionAvailable);

    void setFastActionAvailable(boolean fastActionAvailable);

    void showRedeemPermitView();

    void setTimer(String text);

    void forceExposureEnd();

    void forceBuyingEnd();
}
