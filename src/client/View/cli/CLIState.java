package client.View.cli;

/**
 * This interface marks the main methods of all the State classes.
 * Each one shows a menu - sometimes more than one, depending on the internal state -,
 * processes the input given by the player and invalidate the whole class.
 * The invalidation method is needed when a timeout occurs, so that the internal state
 * of the class and the attributes are reset.
 */
public interface CLIState {
    /**
     * The showMenu method show the option available for the player,
     * printing them to output.
     */
    void showMenu();

    /**
     * The readInput method processes the input given by the player. In particular
     * checks if the input is correct (it should be a number) and if it is calls
     * different methods to elaborate and eventually send an action.
     *
     * @param input is the choice of the player
     * @throws IllegalArgumentException
     */
    void readInput(String input) throws IllegalArgumentException;

    /**
     * The invalidateState resets the internal state of the class and
     * eventually invokes the reset method, which also resets different
     * attributes (not all the classes has such method).
     */
    void invalidateState();
}
