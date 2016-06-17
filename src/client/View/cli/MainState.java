package client.View.cli;

import client.CachedData;
import core.Player;
import core.gamelogic.actions.EndTurnAction;

import java.util.List;
import java.util.Map;

/**
 * Created by Leonardo Arcari on 16/06/2016.
 */
public class MainState implements CLIState {
    private boolean[] mainFast;
    private int maxOptions;
    private CLI cli;

    public MainState(CLI cli) {
        mainFast = new boolean[] {false, false};
        this.cli = cli;
    }

    @Override
    public void showMenu() {
        buildMenu();
        System.out.println("1) Show Gameboard status");
        System.out.println("2) Show Player's status");
        System.out.println("3) End Turn");
        if (mainFast[0] && mainFast[1]) {
            System.out.println("4) Do Main Action");
            System.out.println("5) Do Fast Action");
            maxOptions = 5;
        } else if (mainFast[0] && !mainFast[1]) {
            System.out.println("4) Do Main Action");
            maxOptions = 4;
        } else if (!mainFast[0] && mainFast[1]) {
            System.out.println("4) Do Fast Action");
            maxOptions = 4;
        } else maxOptions = 3;
    }

    @Override
    public void readInput(String input) throws IllegalArgumentException {
        int choice;
        try {
            choice = Integer.valueOf(input);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException();
        }
        if (choice <= 0 || choice > maxOptions) throw new IllegalArgumentException();
        if ((!mainFast[0] || !mainFast[1]) && choice > 3 ) throw new IllegalArgumentException();
        if (choice == 1) {
            cli.setCurrentState(cli.getObjectStatusState());
        }
        else if (choice == 2) {
            cli.setCurrentState(cli.getPlayerState());
        } else if (choice == 3) {
            CachedData.getInstance().getController().sendInfo(new EndTurnAction(
                    (Player) CachedData.getInstance().getMe()
            ));
        } else if (choice == 4 && mainFast[0] && mainFast[1]) {
            cli.setCurrentState(cli.getMainActionState());
        } else if (choice == 4 && !mainFast[0] && mainFast[1]) {
            cli.setCurrentState(cli.getFastActionState());
        } else if (choice == 4 && mainFast[0] && !mainFast[1]) {
            cli.setCurrentState(cli.getMainActionState());
        } else if (choice == 5) {
            cli.setCurrentState(cli.getFastActionState());
        }
    }

    @Override
    public void invalidateState() {
        mainFast[0] = mainFast[1] = false;
    }

    private void buildMenu() {
        if (CachedData.getInstance().myTurnProperty().getValue()) {
            if (CachedData.getInstance().mainActionAvailableProperty().getValue()) {
                mainFast[0] = true;
            } else {
                mainFast[0] = false;
            }
            if (CachedData.getInstance().fastActionAvailableProperty().getValue()) {
                mainFast[1] = true;
            } else {
                mainFast[1] = false;
            }
        } else {
            mainFast[0] = mainFast[1] = false;
        }
        System.out.println("Main: "+ mainFast[0] + " Fast: " + mainFast[1]);
    }
}
