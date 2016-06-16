package client.View.cli;

/**
 * Created by Leonardo Arcari on 14/06/2016.
 */
public interface CLIState {
    void showMenu();
    void readInput(String input) throws IllegalArgumentException;
    void invalidateState();
}
