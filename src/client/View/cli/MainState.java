package client.View.cli;

import client.CachedData;
import core.Player;
import core.gamelogic.actions.EndTurnAction;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is the main menu shown to the player. Here, he can decide to
 * see the game board status, his info's - as coin numbers, permit cards... -,
 * do a normal or fast action and even terminate his turn. Feature of the class
 * is the dynamic menu, that only shows the available actions for the player.
 * If he has no more available main actions, the menu won't show the option, as
 * example. This is done checking for the player's action availability.
 */
public class MainState implements CLIState {
    // Internal state and action availability constants
    private final int END_TURN = 1;
    private final int MAIN_ACTION = 2;
    private final int FAST_ACTION = 3;

    // Reference to the context
    private CLI cli;

    // Attributes of the class
    private Map<Integer, Integer> dynamicOptions;
    private Map<Integer, String> optionStrings;

    /**
     * The constructor sets the context and fills the option strings map, which contains all
     * the possible options that the player can do
     *
     * @param cli is the context owning all the possible game states; it is needed to
     *            change the current state from this class
     */
    public MainState(CLI cli) {
        this.cli = cli;
        dynamicOptions = new HashMap<>();
        optionStrings = new HashMap<>(3);
        optionStrings.put(END_TURN, "End Turn");
        optionStrings.put(MAIN_ACTION, "Do a Main Action");
        optionStrings.put(FAST_ACTION, "Do a Fast Action");
    }

    /**
     * @see CLIState
     */
    @Override
    public void showMenu() {
        buildMenu();
        System.out.println("1) Show Gameboard status");
        System.out.println("2) Show Player's status");
        dynamicOptions.keySet().iterator().forEachRemaining(integer -> {
            System.out.println(integer + ") " + optionStrings.get(dynamicOptions.get(integer)));
        });
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
        if (choice <= 0 || choice > 2 + dynamicOptions.size()) throw new IllegalArgumentException();
        if (choice == 1) {
            cli.setCurrentState(cli.getObjectStatusState());
        }
        else if (choice == 2) {
            cli.setCurrentState(cli.getPlayerState());
        } else doDynamicOption(dynamicOptions.get(choice));
        invalidateState();
    }

    /**
     * @see CLIState
     */
    @Override
    public void invalidateState() {
        dynamicOptions.clear();
    }

    private void buildMenu() {
        dynamicOptions.clear();
        int counter = 3;
        if (CachedData.getInstance().myTurnProperty().getValue()) {
            if (CachedData.getInstance().mainActionAvailableProperty().getValue()) {
                dynamicOptions.put(counter++, MAIN_ACTION);
            }
            if (CachedData.getInstance().fastActionAvailableProperty().getValue()) {
                dynamicOptions.put(counter++, FAST_ACTION);
            }
            dynamicOptions.put(counter, END_TURN);
        }
    }

    private void doDynamicOption(int constChoice) {
        if (constChoice == END_TURN) {
            cli.getController().stopTimer();
            CachedData.getInstance().getController().sendInfo(new EndTurnAction(
                    (Player) CachedData.getInstance().getMe()
            ));
            cli.setCurrentState(cli.getWaitingState());
        } else if (constChoice == MAIN_ACTION) {
            cli.setCurrentState(cli.getMainActionState());
        } else if (constChoice == FAST_ACTION) {
            cli.setCurrentState(cli.getFastActionState());
        }
    }
}
