package client.View.cli;

/**
 * Created by Leonardo Arcari on 16/06/2016.
 */
public class WaitingState implements CLIState{
    private CLI cli;

    public WaitingState(CLI cli) {
        this.cli = cli;
    }

    @Override
    public void showMenu() {
        System.out.println("--- Waiting ---");
    }

    @Override
    public void readInput(String input) throws IllegalArgumentException {
        // Do nothing
    }
}
