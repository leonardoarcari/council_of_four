package client.View.cli;

import client.CachedData;
import core.Player;
import core.gamelogic.actions.Action;
import core.gamelogic.actions.HireServantAction;

/**
 * Created by Matteo on 15/06/16.
 */
public class HireServantState implements CLIState{
    public static final int AVAILABLE = 0;
    public static final int NOT_AVAILABLE = 1;
    private int currentState;

    private CLI cli;

    public HireServantState(CLI cli) {
        currentState = NOT_AVAILABLE;
        this.cli = cli;
    }

    @Override
    public void showMenu() {
        if(CachedData.getInstance().getWealthPath().getPlayerPosition((Player)CachedData.getInstance().getMe()) >= 3) {
            currentState = AVAILABLE;
        } else {
            currentState = NOT_AVAILABLE;
        }

        if(currentState == AVAILABLE) showOptions();
        else showBack();
    }

    @Override
    public void readInput(String input) throws IllegalArgumentException {
        int choice;
        try {
            choice = Integer.valueOf(input);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException();
        }

        if(currentState == AVAILABLE) sendAction(choice);
        else dontSendAction(choice);
    }

    private void showOptions() {
        System.out.println("1) Hire a servant (pay 3 coins)");
        System.out.println("0) Go back");
    }

    private void showBack() {
        System.out.println("Not enough coins (3 required)");
        System.out.println("0) Go back");
    }

    private void sendAction(int choice) {
        if(choice != 1 && choice != 0) throw new IllegalArgumentException();

        if(choice == 1) {
            Action action = new HireServantAction((Player)CachedData.getInstance().getMe());
            CachedData.getInstance().getController().sendInfo(action);
            //TODO change state go back to main menu
        } else {
            //TODO change state go back to fastSelectionState
        }
    }

    private void dontSendAction(int choice) throws IllegalArgumentException {
        if(choice != 0) throw new IllegalArgumentException();

        //TODO change state to go back to fastSelectionState
    }
}
