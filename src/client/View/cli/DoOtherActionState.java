package client.View.cli;

import client.CachedData;
import core.Player;
import core.gamelogic.actions.Action;
import core.gamelogic.actions.AnotherMainActionAction;

/**
 * The class is the state where the player falls when he wants to do the fast
 * action that allows him to do another main action. If he has not enough servants
 * the NOT_AVAILABLE internal state.
 */
public class DoOtherActionState implements CLIState {
    // Internal states of the action
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
    public DoOtherActionState(CLI cli) {
        currentState = NOT_AVAILABLE;
        this.cli = cli;
    }

    /**
     * @see CLIState
     */
    @Override
    public void showMenu() {
        if(CachedData.getInstance().getMe().getServantsNumber() >= 3)
            currentState = AVAILABLE;
        else
            currentState = NOT_AVAILABLE;

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
        currentState = NOT_AVAILABLE;
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
