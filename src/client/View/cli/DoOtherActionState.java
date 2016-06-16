package client.View.cli;

import client.CachedData;
import core.Player;
import core.gamelogic.actions.Action;
import core.gamelogic.actions.AnotherMainActionAction;

/**
 * Created by Matteo on 15/06/16.
 */
public class DoOtherActionState implements CLIState {
    public static final int AVAILABLE = 0;
    public static final int NOT_AVAILABLE = 1;
    private int currentState;

    private CLI cli;

    public DoOtherActionState(CLI cli) {
        currentState = NOT_AVAILABLE;
        this.cli = cli;
    }

    @Override
    public void showMenu() {
        if(CachedData.getInstance().getMe().getServantsNumber() >= 3)
            currentState = AVAILABLE;
        else
            currentState = NOT_AVAILABLE;

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
        System.out.println("1) Do another action (3 servants required)");
        System.out.println("0) Go back");
    }

    private void showBack() {
        System.out.println("Not enough servants (3 required)");
        System.out.println("0) Go back");
    }

    private void sendAction(int choice) throws IllegalArgumentException {
        if(choice != 1 && choice != 0) throw new IllegalArgumentException();

        if(choice == 1) {
            Action action = new AnotherMainActionAction((Player)CachedData.getInstance().getMe());
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
