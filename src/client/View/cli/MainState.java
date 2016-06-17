package client.View.cli;

import client.CachedData;
import core.Player;
import core.gamelogic.actions.EndTurnAction;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Leonardo Arcari on 16/06/2016.
 */
public class MainState implements CLIState {
    private final int END_TURN = 1;
    private final int MAIN_ACTION = 2;
    private final int FAST_ACTION = 3;
    private CLI cli;

    private Map<Integer, Integer> dynamicOptions;
    private Map<Integer, String> optionStrings;

    public MainState(CLI cli) {
        this.cli = cli;
        dynamicOptions = new HashMap<>();
        optionStrings = new HashMap<>(3);
        optionStrings.put(END_TURN, "End Turn");
        optionStrings.put(MAIN_ACTION, "Do a Main Action");
        optionStrings.put(FAST_ACTION, "Do a Fast Action");
    }

    @Override
    public void showMenu() {
        buildMenu();
        System.out.println("1) Show Gameboard status");
        System.out.println("2) Show Player's status");
        dynamicOptions.keySet().iterator().forEachRemaining(integer -> {
            System.out.println(integer + ") " + optionStrings.get(dynamicOptions.get(integer)));
        });
    }

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
