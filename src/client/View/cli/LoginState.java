package client.View.cli;

import core.gamelogic.actions.PlayerInfoAction;

/**
 * Created by Leonardo Arcari on 17/06/2016.
 */
public class LoginState implements CLIState {
    private static final int BASE_STATE = 0;
    private static final int INSERT_USERNAME = 1;
    private static final int INSERT_NICKNAME = 2;
    private static final int CHOOSE_CONNECTION = 3;
    private int internalState;
    private CLI cli;

    private String username;
    private String nickname;

    public LoginState(CLI cli) {
        this.cli = cli;
        internalState = BASE_STATE;
    }

    @Override
    public void showMenu() {
        if (internalState == BASE_STATE) askUserName();
        else if (internalState == INSERT_USERNAME) askNickName();
        else if (internalState == INSERT_NICKNAME) askConnectionType();
    }

    @Override
    public void readInput(String input) throws IllegalArgumentException {
        if (internalState == INSERT_USERNAME) storeUsername(input);
        else if (internalState == INSERT_NICKNAME) storeNickname(input);
        else if (internalState == CHOOSE_CONNECTION) login(input);
    }

    @Override
    public void invalidateState() {
        internalState = BASE_STATE;
    }

    private void askUserName() {
        System.out.println("--- Login ---\n" +
                "\n" +
                "Type your username:");
        internalState = INSERT_USERNAME;
    }

    private void storeUsername(String input) {
        cli.setUsername(input.trim());
    }

    private void askNickName() {
        System.out.println("Type your nickname: ( 0 to go back )");
        internalState = INSERT_NICKNAME;
    }

    private void storeNickname(String input) {
        if (input.trim().equals("0")) internalState = BASE_STATE;
        else cli.setNickname(input.trim());
    }

    private void askConnectionType() {
        System.out.println("Choose your connection type:\n" +
                "1) Socket\n" +
                "2) RMI\n" +
                "0) Back");
        internalState = CHOOSE_CONNECTION;
    }

    private void login(String input) throws IllegalArgumentException{
        int choice;
        try {
            choice = Integer.valueOf(input);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException();
        }
        if (choice == 0) internalState = INSERT_NICKNAME;
        else {
            if (choice == 1) cli.getController().socketConnection();
            else if (choice == 2) cli.getController().rmiConnection();
            cli.setCurrentState(cli.getWaitingState());
        }
    }
}
