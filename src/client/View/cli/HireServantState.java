package client.View.cli;

import client.CachedData;
import core.Player;
import core.gamelogic.actions.Action;
import core.gamelogic.actions.HireServantAction;

/**
 * The class is the state where the player falls when he wants to hire a servant -
 * which is a fast action -. The class contains two inner states, the standard one
 * where the player can fully do the action, and the other where the player cannot
 * do it because he has not enough coins.
 */
public class HireServantState implements CLIState{
    // Inner sates of the action
    private static final int AVAILABLE = 0;
    private static final int NOT_AVAILABLE = 1;

    private int currentState;

    // Reference to the context
    private CLI cli;

    /**
     * The constructor sets the beginning state
     *
     * @param cli is the context owning all the possible game states; it is needed to
     *            change the current state from this class
     */
    public HireServantState(CLI cli) {
        currentState = NOT_AVAILABLE;
        this.cli = cli;
    }

    /**
     * @see CLIState
     */
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

    /**
     * @param input is the choice of the player
     * @see CLIState
     * @throws IllegalArgumentException
     */
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

    /**
     * @see CLIState
     */
    @Override
    public void invalidateState() {
        //Do nothing
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
            cli.setCurrentState(cli.getWaitingState());
        } else {
            cli.setCurrentState(cli.getFastActionState());
        }
    }

    private void dontSendAction(int choice) throws IllegalArgumentException {
        if(choice != 0) throw new IllegalArgumentException();

        cli.setCurrentState(cli.getFastActionState());
    }
}
