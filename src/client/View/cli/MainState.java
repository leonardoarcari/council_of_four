package client.View.cli;

import client.CachedData;

import java.util.List;
import java.util.Map;

/**
 * Created by Leonardo Arcari on 16/06/2016.
 */
public class MainState implements CLIState {
    private boolean[] mainFast;
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
        if (mainFast[0] && mainFast[1]) {
            System.out.println("3) Do Main Action");
            System.out.println("4) Do Fast Action");
        } else if (mainFast[0] && !mainFast[1]) {
            System.out.println("3) Do Main Action");
        } else if (!mainFast[0] && mainFast[1]) {
            System.out.println("3) Do Fast Action");
        }
        System.out.println("0) Back");
    }

    @Override
    public void readInput(String input) throws IllegalArgumentException {
        int choice;
        try {
            choice = Integer.valueOf(input);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException();
        }
        if (choice < 0 || choice > 4) throw new IllegalArgumentException();
        if ((!mainFast[0] || !mainFast[1]) && choice > 3 ) throw new IllegalArgumentException();
        if (choice == 1) {}
        else if (choice == 2) {}
        else if (choice == 3 && mainFast[0] && mainFast[1]) {}
        else if (choice == 3 && !mainFast[0] && mainFast[1]) {}
        else if (choice == 3 && mainFast[0] && !mainFast[1]) {}
        else if (choice == 4) {}
    }

    private void buildMenu() {
        if (CachedData.getInstance().myTurnProperty().get()) {
            if (CachedData.getInstance().mainActionAvailableProperty().get()) {
                mainFast[0] = true;
            } else {
                mainFast[0] = false;
            }
            if (CachedData.getInstance().fastActionAvailableProperty().get()) {
                mainFast[1] = true;
            } else {
                mainFast[1] = false;
            }
        } else {
            mainFast[0] = mainFast[1] = false;
        }
    }
}
