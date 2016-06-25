package client.View.cli;

/**
 * This class acts as an idle state. If the player find himself here,
 * he can press any button but won't exit the WaitingState. So the
 * player is put on hold until the next phase of the game.
 */
public class WaitingState implements CLIState{
    /**
     * @see CLIState
     */
    @Override
    public void showMenu() {
        System.out.println("--- Waiting ---");
    }

    /**
     * @param input is the choice of the player
     * @see CLIState
     * @throws IllegalArgumentException
     */
    @Override
    public void readInput(String input) throws IllegalArgumentException {
        //Do nothing
    }

    /**
     * @see CLIState
     */
    @Override
    public void invalidateState() {
        //Do nothing
    }
}
